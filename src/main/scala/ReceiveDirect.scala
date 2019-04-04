import com.rabbitmq.client._
import Books.{Book, BookStore}
import com.google.gson.{Gson, JsonElement}

object ReceiveDirect {

  private val EXCHANGE_REQ = "direct_req"
  private val EXCHANGE_RES = "direct_res"
  private val severity = "c1"
  private val factory = new ConnectionFactory()
  private val connection = factory.newConnection()
  private val bookStore = new BookStore()
  private val gson = new Gson


  def main(argv: Array[String]) {
    factory.setHost("localhost")
    val channel = connection.createChannel()
    channel.exchangeDeclare(EXCHANGE_REQ, "direct")
    val queueName = channel.queueDeclare().getQueue
    channel.queueBind(queueName, EXCHANGE_REQ, severity)

    println("[*]Waiting for a request message...")
    val deliverCallback: DeliverCallback = (_, delivery) => {
      val message = new String(delivery.getBody, "UTF-8")
      val res=response(message : String)

      //send response message to the client
      val channel2 = connection.createChannel()
      channel2.exchangeDeclare(EXCHANGE_RES, "direct")
      channel2.basicPublish(EXCHANGE_RES, severity, null, res.getBytes("UTF-8"))
      println("sent response for \""+message+"\"")
    }
    channel.basicConsume(queueName, true, deliverCallback, _ => { })
  }

  def response(msg : String): String ={
    val r=msg.split("/")

    // list all the books
     if (r(0)=="all"){
       val books = bookStore.listAllBooks()
       return gson.toJson(books)

     // Show book details
     }else if(r(0)=="bookDetails") {
       val book = bookStore.bookDetails(r(1).toInt);
       val response = gson.toJson(book);
       return response
      // add book into bookstore
     }else if(r(0)=="addBook"){

       val jsonString = gson.fromJson(r(1), classOf[JsonElement])
       val json = jsonString.getAsJsonObject
       val name = json.get("name").getAsString
       val author = json.get("author").getAsString
       val lang = json.get("lang").getAsString
       val pages = json.get("pages").getAsInt

       val newBook = new Book(name, author, lang, pages)
       bookStore.addBook(newBook)
       val response = "Book is saved successfully\n"
       return response
     }else{
       return "Not Found"
     }
  }
}
