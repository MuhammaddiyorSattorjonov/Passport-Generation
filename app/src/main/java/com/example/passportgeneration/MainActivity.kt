package com.example.passportgeneration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.passportgeneration.databinding.ActivityMainBinding
import com.example.passportgeneration.db.AppDataBase
import com.example.passportgeneration.models.MyPerson
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.Collections

class MainActivity : AppCompatActivity() {
    lateinit var adapter: RvAdapter
    private lateinit var deletedTempUser: MyPerson
    lateinit var myPerson: MyPerson
    private lateinit var file:File
    var isDeleted = false
    var position = 0
    private lateinit var appDataBase: AppDataBase
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myPerson = MyPerson()
        appDataBase = AppDataBase.getInstance(this)
        adapter = RvAdapter(appDataBase.myDao().getAllPerson() as ArrayList,this)
        binding.rv.adapter = adapter
        ItemTouchHelper(simpleCallBack).attachToRecyclerView(binding.rv)


        binding.swipe.setOnRefreshListener {
            adapter.notifyDataSetChanged()
            binding.swipe.isRefreshing = false
        }
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0){
                    binding.plus.hide()
                }else{
                    binding.plus.show()
                }
            }
        })

        binding.plus.setOnClickListener {
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }
    }

    private var simpleCallBack = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
        ItemTouchHelper.RIGHT.or(ItemTouchHelper.LEFT)
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            var user = adapter.list[viewHolder.adapterPosition]
            user.position = target.adapterPosition
            appDataBase.myDao().updateUser(user)

            Collections.swap(
                adapter.list,
                viewHolder.adapterPosition,
                target.adapterPosition
            )
            adapter.notifyItemMoved(
                viewHolder.adapterPosition,
                target.adapterPosition
            )
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            position = viewHolder.adapterPosition
            file = File(
                this@MainActivity.filesDir,
                "${adapter.list[position].id}.jpg"
            )
            deletedTempUser = adapter.list[position]
            adapter.list.removeAt(position)
            adapter.notifyItemRemoved(position)
            isDeleted = true

            Snackbar.make(binding.rv,"${deletedTempUser.name} o'chilrildi",Snackbar.LENGTH_LONG)
                .setAction("Undo"){
                    isDeleted = false
                    adapter.list.add(position,deletedTempUser)
                    adapter.notifyItemInserted(position)
                }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (isDeleted){
                            appDataBase.myDao().delete(deletedTempUser)
                            file.delete()
                        }
                    }
                }).show()
        }

    }

    override fun onPause() {
        super.onPause()
        if (isDeleted){
            appDataBase.myDao().delete(deletedTempUser)
            file.delete()
        }
    }
}
