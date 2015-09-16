using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using smartwardrobe.Models;
using System.Text;
using System.Configuration;
using System.Security.Cryptography;
using System.Web;
using System.Globalization;
using Newtonsoft.Json;
using System.Runtime.Serialization.Json;
using Microsoft.Azure.NotificationHubs;

namespace smartwardrobe.Controllers
{

    /// <summary>
    /// Main Controller functino for Cloth's data
    /// </summary>
    public class NewClothNotification{
        public string RFID { get; set; }

    }
    public class ClothesController : ApiController
    {
        private smartwardrobeContext db = new smartwardrobeContext();

        // GET: api/Clothes
        public IQueryable<Cloth> GetClothes()
        {
            return db.Clothes;
        }

        // GET: api/Clothes/5
        [ResponseType(typeof(Cloth))]
        public IHttpActionResult GetCloth(string rfid)
        {
            if (!ClothExists(rfid)) {
                return NotFound();
            }
            Cloth cloth = db.Clothes.Where(o=>o.rfid.Equals(rfid)).First();
            if (cloth == null)
            {
                return NotFound();
            }

            return Ok(cloth);
        }

        // PUT: api/Clothes/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutCloth(string rfid, Cloth cloth)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (rfid != cloth.rfid)
            {
                return BadRequest();
            }

            db.Entry(cloth).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ClothExists(rfid))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/Clothes
        [ResponseType(typeof(Cloth))]
        public IHttpActionResult PostCloth(Cloth cloth)
        {
            if (!ClothExists(cloth.rfid))
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                cloth.createdAt = DateTime.Now;
                cloth.lastUpdatedAt = DateTime.Now;
                db.Clothes.Add(cloth);
                db.SaveChanges();
                //update to service bus to notify mobile app
                SendNotificationAsync(cloth.rfid);
            }
            else
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                Cloth updcloth = db.Clothes.Where(o=>o.rfid.Equals(cloth.rfid)).First();
                updcloth.image = cloth.image;
                updcloth.name = string.IsNullOrEmpty(cloth.name) ? updcloth.name:cloth.name ;
                updcloth.status = string.IsNullOrEmpty(cloth.status)? updcloth.status: cloth.status;
                updcloth.type = string.IsNullOrEmpty(cloth.type)?updcloth.type:cloth.type;
                updcloth.color = (!string.IsNullOrEmpty(cloth.color))? cloth.color: updcloth.color;
                updcloth.frequency = (cloth.frequency!=0 && cloth.frequency!=updcloth.frequency)?cloth.frequency: updcloth.frequency;
                updcloth.lastUpdatedAt = DateTime.Now;
                db.Entry(updcloth).State = EntityState.Modified;

                try
                {
                    db.SaveChanges();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ClothExists(cloth.rfid))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }

            }
            return CreatedAtRoute("DefaultApi", new { id = cloth.id }, cloth);
        }

        // DELETE: api/Clothes/5
        [ResponseType(typeof(Cloth))]
        public IHttpActionResult DeleteCloth(int id)
        {
            Cloth cloth = db.Clothes.Find(id);
            if (cloth == null)
            {
                return NotFound();
            }

            db.Clothes.Remove(cloth);
            db.SaveChanges();

            return Ok(cloth);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ClothExists(string id)
        {
            return db.Clothes.Count(e => e.rfid == id) > 0;
        }
        private static string createToken(string resourceUri, string keyName, string key)
        {
            TimeSpan sinceEpoch = DateTime.UtcNow - new DateTime(1970, 1, 1);
            var expiry = Convert.ToString((int)sinceEpoch.TotalSeconds + 3600); //EXPIRES in 1h 
            string stringToSign = HttpUtility.UrlEncode(resourceUri) + "\n" + expiry;
            HMACSHA256 hmac = new HMACSHA256(Encoding.UTF8.GetBytes(key));

            var signature = Convert.ToBase64String(hmac.ComputeHash(Encoding.UTF8.GetBytes(stringToSign)));
            var sasToken = String.Format(CultureInfo.InvariantCulture,
            "SharedAccessSignature sr={0}&sig={1}&se={2}&skn={3}",
                HttpUtility.UrlEncode(resourceUri), HttpUtility.UrlEncode(signature), expiry, keyName);

            return sasToken;
        }
        private static async void SendNotificationAsync(string rfid)
        {
            // Define the notification hub.
            NotificationHubClient hub =
            NotificationHubClient.CreateClientFromConnectionString(
                "<Use your key>", "https://smartwardrobe.servicebus.windows.net/notification"
                );

            try
            {
               
            var notification = "{\"data\":{\"rfid\":\"" + rfid + "\"}}";
            await hub.SendGcmNativeNotificationAsync(notification, rfid);
            }
            catch (ArgumentException)
            {
                // An exception is raised when the notification hub hasn't been 
                // registered for the iOS, Windows Store, or Windows Phone platform. 
            }
        }
    }
}