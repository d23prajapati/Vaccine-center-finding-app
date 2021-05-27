package com.example.vaccinefinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class VaccineListAdapter(private val centerListItems: ArrayList<Center>): RecyclerView.Adapter<VaccineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_vaccine, parent, false)
        return VaccineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val currentItem = centerListItems[position]

        holder.centreName.text = currentItem.centerName
        holder.address.text = currentItem.address
        holder.timing.text = currentItem.from
        holder.feeType.text = currentItem.feeType
        holder.fee.text = currentItem.fee
        holder.dose1.text = currentItem.dose1
        holder.dose2.text = currentItem.dose2
        holder.age.text = "${currentItem.age}+"
    }

    override fun getItemCount(): Int {
        return centerListItems.size
    }

    fun updateCenter(updatedCenters: ArrayList<Center>){
        centerListItems.clear()
        centerListItems.addAll(updatedCenters)

        notifyDataSetChanged()
    }
}

class VaccineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val centreName = itemView.findViewById<TextView>(R.id.center_name)
    val address = itemView.findViewById<TextView>(R.id.address)
    val timing = itemView.findViewById<TextView>(R.id.timing)
    val feeType = itemView.findViewById<TextView>(R.id.fee_type)
    val fee = itemView.findViewById<TextView>(R.id.fee)
    val dose1 = itemView.findViewById<TextView>(R.id.available_dose_1)
    val dose2 = itemView.findViewById<TextView>(R.id.available_dose_2)
    val vaccine = itemView.findViewById<TextView>(R.id.vaccine_name)
    val age = itemView.findViewById<TextView>(R.id.age_limit)
}