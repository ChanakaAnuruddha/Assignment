package Books

class Book(private var name:String, private var author:String , private var lang :String, private var num_of_pages: Int) {

  def SetName(name:String): Unit ={
    this.name=name
  }

  def SetAuthor(author:String): Unit ={
    this.author=author
  }

  def Setlang(lang:String): Unit ={
    this.lang=lang
  }

  def setNumOfPage(pages:String): Unit ={
    this.num_of_pages=num_of_pages
  }

  def getName(): String ={
    return this.name
  }

  def getAuthor(): String ={
    return this.author
  }

  def getlang(): String ={
    return this.lang
  }

  def getNumOfPage(): Int ={
    return this.num_of_pages
  }

}
