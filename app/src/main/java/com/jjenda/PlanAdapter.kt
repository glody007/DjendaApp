package com.jjenda

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jjenda.databinding.PlanBinding
import com.jjenda.reseau.Plan


class PlanAdapter(val clickListener : PlanListener) : RecyclerView.Adapter<PlanAdapter.PlansViewHolder>() {
    var plans = listOf<Plan>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansViewHolder {
        Log.d("Plan", "create")
        return PlansViewHolder.from(parent)
    }

    override fun onBindViewHolder(plansViewHolderholder: PlansViewHolder, position: Int) {
        val plan = plans[position]
        plansViewHolderholder.bind(plan, clickListener)
    }

    override fun getItemCount() = plans.size

    class PlansViewHolder private constructor(val binding: PlanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plan : Plan, clickListener: PlanListener) {
            binding.plan = plan
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): PlansViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return PlansViewHolder(PlanBinding.inflate(inflater, parent, false))
            }
        }

    }
}

class PlanListener(val clickListener : (type: String) -> Unit) {
    fun onclick(plan: Plan) = clickListener(plan.type)
}