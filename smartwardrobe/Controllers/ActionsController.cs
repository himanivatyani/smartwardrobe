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

namespace smartwardrobe.Controllers
{
    /// <summary>
     /// Actions controller to handing cloth activities(take out or put in)
     /// </summary>
    public class ActionsController : ApiController
    {
        private smartwardrobeContext db = new smartwardrobeContext();

        // GET: api/Actions
        public IQueryable<Actions> GetActions()
        {
            return db.Actions;
        }

        // GET: api/Actions/5
        [ResponseType(typeof(Actions))]
        public IHttpActionResult GetActions(int id)
        {
            Actions actions = db.Actions.Find(id);
            if (actions == null)
            {
                return NotFound();
            }

            return Ok(actions);
        }

        // PUT: api/Actions/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutActions(int id, Actions actions)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != actions.id)
            {
                return BadRequest();
            }

            db.Entry(actions).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ActionsExists(id))
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

        // POST: api/Actions
        [ResponseType(typeof(Actions))]
        public IHttpActionResult PostActions(Actions actions)
        {
            //if rfid is empty string meaning all clothes in the db are back to wardrobe
            if (actions.rfid.Equals(""))
            {
                //check any cloth with status = '-1'(taken out)
                var clothOut = db.Clothes.Where(c => c.status.Equals("-1"));
                foreach (Cloth clt in clothOut)
                {
                    //add an action for putting in cloth, update cloth status
                    Actions actionAdd = new Actions();
                    actionAdd.rfid = clt.rfid;
                    actionAdd.status = "1";
                    actionAdd.createdAt = DateTime.Now;
                    clt.status = "1";
                    db.Actions.Add(actionAdd);
                    db.Entry(clt).State = EntityState.Modified;
                    db.SaveChanges();
                }
            }
            else
            { 
                //take out a cloth
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                //need to update cloth table for the latest status
                Cloth clt = db.Clothes.Where(c => c.rfid.Equals(actions.rfid)).First();
                clt.status = actions.status;
                if (actions.status == "-1")
                {
                    //also update the frequency of wearing:
                    clt.frequency += 1;
                }
                db.Entry(clt).State = EntityState.Modified;
                db.Actions.Add(actions);
                db.SaveChanges();
            }
            return CreatedAtRoute("DefaultApi", new { id = actions.id }, actions);
        }

        // DELETE: api/Actions/5
        [ResponseType(typeof(Actions))]
        public IHttpActionResult DeleteActions(int id)
        {
            Actions actions = db.Actions.Find(id);
            if (actions == null)
            {
                return NotFound();
            }

            db.Actions.Remove(actions);
            db.SaveChanges();

            return Ok(actions);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ActionsExists(int id)
        {
            return db.Actions.Count(e => e.id == id) > 0;
        }
    }
}