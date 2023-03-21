package com.example.passportgeneration

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.passportgeneration.databinding.ItemRvBinding
import com.example.passportgeneration.models.MyPerson

class RvAdapter(val list: ArrayList<MyPerson>, val context: Context) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(val itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(myPerson: MyPerson, position: Int) {
            if (list.isNotEmpty()) {
                itemRvBinding.name.text = myPerson.name
                itemRvBinding.passportSeriya.text = myPerson.seriyaPassport
                itemRvBinding.imageProfile.setImageBitmap(BitmapFactory.decodeFile(myPerson.image))
                itemRvBinding.name.setOnClickListener {
                    Toast.makeText(context, "$myPerson", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

}