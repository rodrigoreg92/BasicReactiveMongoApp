package models
import play.api.libs.json.Json

/**
  * Created by davidorto on 7/02/16.
  */
case class Persona ( nombre:String , apellido:String , edad:Int , genero:String)

object PersonaConvertidor{

  implicit val personRead = Json.reads[Persona]
  implicit val personWrite = Json.writes[Persona]

}
