package com.example.homework3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework3.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        productAdapter = ProductAdapter { product ->
            Toast.makeText(
                this,
                "${product.name} - ₾${product.price}",
                Toast.LENGTH_SHORT
            ).show()
        }


        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productAdapter
            setHasFixedSize(true)
        }


        database = FirebaseDatabase.getInstance().getReference("products")
        observeProducts()
    }

    private fun observeProducts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<Product>()

                for (productSnap in snapshot.children) {
                    val product = productSnap.getValue(Product::class.java)
                    product?.let { products.add(it) }
                }


                productAdapter.submitList(products)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Error loading data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
