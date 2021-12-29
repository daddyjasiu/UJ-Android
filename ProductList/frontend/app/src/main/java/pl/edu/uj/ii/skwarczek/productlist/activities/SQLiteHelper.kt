package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pl.edu.uj.ii.skwarczek.productlist.models.ProductModel

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "products.db"
        private const val TABLE_PRODUCT = "table_product"
        private const val ID = "id"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableProduct = ("CREATE TABLE " + TABLE_PRODUCT + "("
                + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + DESCRIPTION + " TEXT" + ")")
        db?.execSQL(createTableProduct)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        onCreate(db)
    }

    fun insertProduct(product: ProductModel): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, product.id)
        contentValues.put(NAME, product.name)
        contentValues.put(DESCRIPTION, product.description)

        val success = db.insert(TABLE_PRODUCT, null, contentValues)
        db.close()

        return success
    }

    fun getAllProducts(): ArrayList<ProductModel>{
        val productList: ArrayList<ProductModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_PRODUCT"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        }
        catch(e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var description: String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                description = cursor.getString(cursor.getColumnIndexOrThrow("description"))

                val product = ProductModel(id = id, name = name, description = description)
                productList.add(product)
            }while(cursor.moveToNext())
        }

        return productList
    }

    fun updateProduct(product: ProductModel): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, product.id)
        contentValues.put(NAME, product.name)
        contentValues.put(DESCRIPTION, product.description)

        val success = db.update(TABLE_PRODUCT, contentValues, "id=" + product.id, null)
        db.close()
        return success
    }

    fun deleteProductById(id: Int): Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TABLE_PRODUCT, "id=$id", null)
        db.close()

        return success
    }

}