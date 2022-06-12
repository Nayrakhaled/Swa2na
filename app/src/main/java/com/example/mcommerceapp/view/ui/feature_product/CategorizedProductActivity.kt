package com.example.mcommerceapp.view.ui.feature_product

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mcommerceapp.MainActivity
import com.example.mcommerceapp.databinding.CategorizedProductScreenBinding
import com.example.mcommerceapp.model.Keys
import com.example.mcommerceapp.model.currency_repository.CurrencyRepo
import com.example.mcommerceapp.model.remote_source.RemoteSource
import com.example.mcommerceapp.model.shopify_repository.product.ProductRepo
import com.example.mcommerceapp.view.ui.favorite_product.view.FavoriteScreen
import com.example.mcommerceapp.view.ui.feature_product.adapter.CategorizedProductAdapter
import com.example.mcommerceapp.view.ui.feature_product.adapter.OnClickListner
import com.example.mcommerceapp.view.ui.feature_product.viewmodel.CategorizedProductVM
import com.example.mcommerceapp.view.ui.feature_product.viewmodel.CategorizedProductVMFactory
import com.example.mcommerceapp.view.ui.product_detail.view.ProductDetail
import com.example.mcommerceapp.view.ui.search.SearchActivity
import com.example.mcommerceapp.view.ui.shopping_cart.view.ShoppingCartScreen

class CategorizedProductActivity : AppCompatActivity(), OnClickListner {

    lateinit var binding: CategorizedProductScreenBinding

    private lateinit var productsVM: CategorizedProductVM
    private lateinit var productsVMFactory: CategorizedProductVMFactory
    private lateinit var vendor: String
    private lateinit var value: String
    private lateinit var categoryProductAdapter :CategorizedProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CategorizedProductScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        val intent = intent.getBundleExtra("PRODUCTS")
        vendor = intent?.getString("VENDOR", " ") ?: ""
        value = intent?.getString("VALUE", " ") ?: ""

        when (intent?.get("TYPE").toString()) {
            Keys.VENDOR -> observeVendor()
            Keys.ALL_PRODUCT -> observeAllProducts()
        }

        binding.actionBarLayout.backImg.visibility = ImageView.VISIBLE

        binding.actionBarLayout.backImg.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

        binding.actionBarLayout.favouriteImage.setOnClickListener { startActivity(Intent(this, FavoriteScreen::class.java)) }

        binding.actionBarLayout.cardImage.setOnClickListener { startActivity(Intent(this, ShoppingCartScreen::class.java)) }

        binding.actionBarLayout.searchImage.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }

        productsVM.products.observe(this) {
            categoryProductAdapter.setData(it, productsVM.currencySymbol, productsVM.currencyValue)
            binding.grid.adapter = categoryProductAdapter
        }
    }


    private fun observeVendor() {
        productsVM.getProductsVendor(value)
    }
    private fun observeAllProducts() {
        productsVM.allProducts.observe(this) {
            categoryProductAdapter.setData(it, productsVM.currencySymbol, productsVM.currencyValue)
            binding.grid.adapter = categoryProductAdapter
        }
    }
    private fun init(){
        productsVMFactory = CategorizedProductVMFactory(ProductRepo.getInstance(RemoteSource()), CurrencyRepo.getInstance(RemoteSource(), this))
        productsVM = ViewModelProvider(this, productsVMFactory)[CategorizedProductVM::class.java]
        categoryProductAdapter = CategorizedProductAdapter(this, this)
    }
    override fun onClick(id: String) {
        val intent = Intent(this, ProductDetail::class.java)
        intent.putExtra("PRODUCTS_ID", id)
        startActivity(intent)
    }
}