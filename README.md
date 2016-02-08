## Synopsis
Basic play CRUD API project , it was made to teach new scala developers
how to configure a simple play 2.4.x project using scala and reactiveMongo library
reactiveMongo is used to give an async connection to mongo db.

## Code Example

Basic create operation over a Person in a mongo db collection:

```scala
def createNewPerson = Action.async(parse.json) { request =>
    request.body.validate[Persona].map { person =>
      collection.insert(person).map { lastError =>
        Logger.debug(s"Successfully inserted with LastError: $lastError")
        Ok("Persona creada exitosamnete !")
      }
    }.getOrElse(Future.successful(BadRequest("Peticion invalida")))
  }
```

Query to find a person by their last name:

```scala
def findPersonByName(lastName: String) = Action.async {
    val cursor: Cursor[Persona] = collection.
      find(Json.obj("apellido" -> lastName)).
      cursor[Persona]()
    val futureUsersList: Future[List[Persona]] = cursor.collect[List]()
    futureUsersList.map { persons =>
      Ok(persons.toString)
    }
  }
```

## Installation

> As for Java JDK 1.8 , Sbt 0.13.x and mongodb 3.x is required to use this project

- create a mongo collection called Persona and follow this json structure:

```json
{
    "nombre" : "maria",
    "apellido" : "cortes",
    "edad" : 20,
    "genero" : "F"
}
```
- download the project and run over the project directory:

sbt run

- to test if is working just go to your preference browser and write:

localhost:9000/

A success message shall appear.

## API Reference

For a better documentation, read official play and  reactivemongo docs:

> [Play Framework](http://playframework.com/) , [ReactiveMongo](http://reactivemongo.org/releases/0.11/documentation/tutorial/play2.html)


