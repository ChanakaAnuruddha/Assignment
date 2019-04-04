package Books

class BookStore() {

  var currentId=0

  private var Booklist:Map[Int,Book] = Map()

  var book1 = new Book("Norwegian Wood", "Haruki Murakami", "English",336);
  var book2 = new Book("Wonder", "R. J. Palacio", "English",280);
  var book3 = new Book("The Wind-Up Bird Chronicle", " Haruki Murakami", "English",345);
  var book4 = new Book("Bridge to Terabithia", "Katherine Paterson", "English",874);
  var book5 = new Book("The Kite Runner", "Khaled Hosseini", "English",332);
  addBook((book1))
  addBook((book2))
  addBook((book3))
  addBook((book4))
  addBook((book5))

  def addBook(newBook: Book) {
    Booklist += (currentId -> newBook)
    currentId+=1
  }

  def listAllBooks():Array[String]={
    var BookNames =new Array[String](Booklist.size)
    Booklist.foreach {case(key, book) =>
      BookNames(key)= book.getName()
    }
    return BookNames
  }

  def bookDetails(id :Int):Book={
    return Booklist(id)
  }

  def numOfBooks(): Int ={
    return Booklist.size
  }
}

object Demo{
  def main(args: Array[String]): Unit ={

    var store = new BookStore();

    // add new book
    var newBook = new Book("The Chocolate War", "Robert Cormier", "English",456);

    store.addBook(newBook)

    // Book Details
    println("Enter the book id (Integer):-")
    val id=scala.io.StdIn.readInt()
    val book=store.bookDetails(id)

    val name= book.getName()
    val author=book.getAuthor()
    val lang = book.getlang()
    val pages = book.getNumOfPage()

    println("Book Name: "+name)
    println("Author: "+author)
    println("Language: "+lang)
    println("Pages: "+pages)
    println

    // Show Book List
    val booklist=store.listAllBooks()
    println("====list of all books====")
    for (book <- booklist) println(book)
  }
}