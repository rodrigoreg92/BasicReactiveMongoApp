package models

import play.api.libs.functional.FunctionalBuilder
import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by davidorto on 7/02/16.
  */
case class Persona ( nombre:String , apellido:String , edad:Int , genero:String)

object PersonaConvertidor{

  implicit val personRead = Json.reads[Persona]
  implicit val personWrite = Json.writes[Persona]


}
