package it.uninsubria.patientkiosk.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import it.uninsubria.patientkiosk.R
import it.uninsubria.patientkiosk.model.Questionnaire

class QuestionnaireAdapter(
    private val context: Context,
    private var items: List<Questionnaire>
) : BaseAdapter() {

    fun update(newItems: List<Questionnaire>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Questionnaire = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_questionnaire, parent, false)

        val item = getItem(position)
        view.findViewById<TextView>(R.id.itemTitleText).text = item.title
        view.findViewById<TextView>(R.id.itemDescriptionText).text = item.description
        view.findViewById<TextView>(R.id.itemMetaText).text =
            "${item.questions.size} domande · massimo ${item.maxScore} punti"

        return view
    }
}
