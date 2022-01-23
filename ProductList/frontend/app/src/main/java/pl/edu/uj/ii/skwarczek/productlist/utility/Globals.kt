package pl.edu.uj.ii.skwarczek.productlist.utility

object Globals {
    private var waiter: Boolean = true

    fun set(x: Boolean){
        waiter = x
    }

    fun get() : Boolean{
        return waiter
    }
}