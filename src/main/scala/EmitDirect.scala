import com.rabbitmq.client.{ConnectionFactory, DeliverCallback}

object EmitDirect {

  private val EXCHANGE_REQ = "direct_req"
  private val EXCHANGE_RES = "direct_res"
  private val severity = "c1"
  private val factory = new ConnectionFactory()
  private val connection = factory.newConnection()

  def main(argv: Array[String]) {
    factory.setHost("localhost")

    //send request message
    request()

    //get response
    response()
  }


  private def getMessage(): String = {
    println("===================================================")
    println("Enter the service number")
    println("1) All the Books   2) A Book Details   3) Add Book ")
    val a=scala.io.StdIn.readInt()

    if (a == 1) return "all"
    else  if(a == 2) return "bookDetails/"+getBookID()
    else  if (a == 3) return "addBook/"+getBookInput()
    else return " "
  }

  private def getBookID(): String ={
    println("Enter Book ID")
    val a=scala.io.StdIn.readInt()
    return a.toString
  }

  private def getBookInput(): String ={
    println("Enter Book name.")
    val name = scala.io.StdIn.readLine()
    println("Enter author.")
    val author = scala.io.StdIn.readLine()
    println("Enter language.")
    val lang = scala.io.StdIn.readLine()
    println("Enter number of pages(Integer).")
    val pages = scala.io.StdIn.readInt()

    return "{\"name\":"+name+",\"author\":"+author+",\"lang\":"+lang+",\"pages\":"+pages+"}"
  }

  private def request(): Unit ={
    val channel = connection.createChannel()
    channel.exchangeDeclare(EXCHANGE_REQ, "direct")
    val message = getMessage()
    println("request is sending...")
    channel.basicPublish(EXCHANGE_REQ, severity, null, message.getBytes("UTF-8"))
  }

  private def response(): Unit ={
        val channel2 = connection.createChannel()
        channel2.exchangeDeclare(EXCHANGE_RES, "direct")
        val queueName2 = channel2.queueDeclare().getQueue
        channel2.queueBind(queueName2, EXCHANGE_RES, severity)
        val deliverCallback: DeliverCallback = (_, delivery) => {
          val response = new String(delivery.getBody, "UTF-8")
          println(response)
          //getting ready for another request message
          request()
        }
        println("Waiting for the response...")
        channel2.basicConsume(queueName2, true, deliverCallback, _ => { })
  }
}