package controllers

import javax.inject.Inject


import play.api.mvc._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.{ReactiveMongoApi, MongoController, ReactiveMongoComponents}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Logger
import reactivemongo.bson.BSONDocument
import scala.concurrent.Future
import reactivemongo.api.Cursor
import play.api.libs.json._
import reactivemongo.play.json._
import play.modules.reactivemongo.json.collection._

import scala.util.{Failure, Success}


/**
  * Controlador principal de la aplaicion,
  * se le inyectan los modulos de reactivemongo
  * para usar el api
  */
class Application @Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {


  /**
    * llama a la collection de personas desde mongo,
    * todas las operaciones se realizan con este objeto
    *
    * @return una jsonCollection de la collection personas
    */
  def collection: JSONCollection = db.collection[JSONCollection]("Personas")

  // ------------------------------------------ //
  // Importa los los modelos con sus convertidores //
  // ------------------------------------------ //
  import models.PersonaConvertidor._
  import models._

  /**
    * Verifica que el servidor esta funcionanco
    */
  def index = Action { Ok("Todo esta funcionando") }

  /**
    * Crea una persona nueva en la base de datos,
    * mapea el objeto enviado por POST a una entidad  Persona,
    * utiliza el reader del companion object de Persona,
    */
  def createNewPerson = Action.async(parse.json) { request =>
    request.body.validate[Persona].map { person =>
      collection.insert(person).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Ok("Persona creada exitosamnete !")
      }
    }.getOrElse(Future.successful(BadRequest("Peticion invalida")))
  }

  /**
    * actualiza la info de la persona a partir del apellido seleccionado
    *
    * @param lastName apellido de la persona
    * @return documento completo de la persona
    */

  def updatePerson(lastName: String) = Action.async(parse.json) { request =>
    request.body.validate[Persona].map { person =>
      val selector = BSONDocument("apellido" -> lastName)
      val modifier = BSONDocument(
      "$set" -> BSONDocument(
        "nombre" -> person.nombre,
        "edad" -> person.edad,
        "genero" -> person.genero))
      println(lastName)
      collection.update(selector ,modifier).map { lastError =>
        Logger.debug(s"Successfully updated with LastError: $lastError")
        Ok("Persona Actualizada exitosamnete !")
      }
    }.getOrElse(Future.successful(BadRequest("Peticion invalida")))
  }


  /**
    * elimina la persona a partir del apellido seleccionado
    *
    * @param lastName apellido de la persona
    * @return documento completo de la persona
    */

  def deletePerson(lastName: String) = Action.async { request =>
      val selector = BSONDocument("apellido" -> lastName)
      collection.remove(selector).map { lastError =>
        Logger.debug(s"Successfully deleted with LastError: $lastError")
        Ok("Persona Eliminada exitosamnete !")
      }
   }


    /**
      * Retorna la persona del apellido seleccionado
      *
      * @param lastName apellido de la persona
      * @return documento completo de la persona
      */
    def findPersonByName(lastName: String) = Action.async {

      val cursor: Cursor[Persona] = collection.
        find(Json.obj("apellido" -> lastName)).
        cursor[Persona]()

      val futureUsersList: Future[List[Persona]] = cursor.collect[List]()

      futureUsersList.map { persons =>
        Ok(persons.toString)
      }
    }

    /**
      * Retorna Todas las personas de la collection
      *
      * @return todos los documentos completo de la persona
      */
    def findAllPersons = Action.async {

      val cursor: Cursor[Persona] = collection.
        find(Json.obj()).
        cursor[Persona]()

      val futureUsersList: Future[List[Persona]] = cursor.collect[List]()

      futureUsersList.map { persons =>
        Ok(persons.toString)
      }
    }
  }


