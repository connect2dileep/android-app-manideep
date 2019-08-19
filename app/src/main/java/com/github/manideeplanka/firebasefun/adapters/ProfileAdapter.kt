package com.github.manideeplanka.firebasefun.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.manideeplanka.firebasefun.R
import com.google.firebase.firestore.*

open class ProfileAdapter(private var query : Query?) : RecyclerView.Adapter<ProfileAdapter.VH>(), EventListener<QuerySnapshot> {
    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
        if (p1 != null) {
            Log.e("Profile Adapter",p1.message)
            return
        }

        for (change in p0!!.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }
        onDataChanged()
    }

    private fun onDocumentAdded(change: DocumentChange?) {
        snapshots.add(change!!.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    protected fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            snapshots.set(change.oldIndex, change.document)
            notifyItemChanged(change.oldIndex)
        } else {
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    protected fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    private fun onDataChanged() {}

    private val snapshots = ArrayList<DocumentSnapshot>()

    private var registration: ListenerRegistration? = null

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(snapshot: DocumentSnapshot) {
            nameTextView.text = snapshot.get("name").toString()
            ageTextView.text = snapshot.get("age").toString()
            addressTextView.text = snapshot.get("address").toString()
        }

        var nameTextView : TextView = itemView.findViewById(R.id.name_item_tv)
        var ageTextView : TextView = itemView.findViewById(R.id.age_item_tv)
        var addressTextView : TextView = itemView.findViewById(R.id.address_item_tv)

    }

    fun startListening() {
        if(query != null && registration == null) {
            registration = query?.addSnapshotListener(this)
        }
    }

    fun stopListening() {
        if(registration != null) {
            registration?.remove()
            registration = null
        }

        snapshots.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        return VH(
            inflater.inflate(
                R.layout.item_profile,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        Log.d("Profile Adapter","item count: ${snapshots.size}")
        return snapshots.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getSnapshot(position))
    }

    private fun getSnapshot(index: Int): DocumentSnapshot {
        Log.d("Profile Adapter",snapshots[index].data.toString())
        return snapshots[index]
    }

}