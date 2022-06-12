package com.example.mcommerceapp.view.ui.addresses.view.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mcommerceapp.databinding.ItemAddressBinding
import com.example.mcommerceapp.pojo.customers.Addresses


class AddressesAdapter(var context: Context, private var listener: AddressesCommunicator) :
    RecyclerView.Adapter<AddressesAdapter.ViewHolder>() {
    private var orderList: ArrayList<Addresses> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = orderList[position]
        holder.binding.apply {
            cityTextView.text = "${currentItem.address1} , ${currentItem.city}"
            zipCodeTextView.text = currentItem.zip
            countryTextView.text = currentItem.country
        }
        if (currentItem.default == true) {
            Log.i(
                "TAG",
                "onBindViewHolder:    ${currentItem.city}  ${currentItem.default.toString()}"
            )
            holder.itemView.setBackgroundColor(Color.CYAN)
        }
        holder.binding.deleteAddress.setOnClickListener {
            if (currentItem.default == true)
                Toast.makeText(
                    context,
                    "Cannot delete your default address",
                    Toast.LENGTH_SHORT
                ).show()
            else listener.deleteAddress(currentItem.id.toString())
        }
        holder.itemView.setOnClickListener {
            listener.setDefaultAddress(currentItem.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return orderList.count()
    }

    fun setData(orderList: ArrayList<Addresses>) {
        this.orderList = ArrayList<Addresses>()
        notifyDataSetChanged()
        this.orderList = orderList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root)

}