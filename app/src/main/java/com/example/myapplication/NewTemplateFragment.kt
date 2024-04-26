package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentNewTemplateBinding

class NewTemplateFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentNewTemplateBinding? = null

    private val binding get() = _binding!!

    private lateinit var templateTitle: EditText
    private lateinit var gameTypeText: EditText
    private lateinit var numberOfPlayers: EditText
    private lateinit var numberOfRows: EditText
    private lateinit var previewLayout: LinearLayout

    //private lateinit var rowHeaders: Array<Array<EditText?>>
    private lateinit var createTemplateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        // Initialize views
        templateTitle = binding.TemplateTitle
        gameTypeText = binding.GameTypeText
        numberOfPlayers = binding.editTextNumberSigned
        numberOfRows = binding.editTextNumberSigned2
        previewLayout = binding.PreviewLayout
        createTemplateButton = binding.createTemplateButton

        // Add text change listeners to update preview

        //rowHeaders = Array(numRows) { Array(numPlayers) { null } }


        numberOfPlayers.addTextChangedListener {
            updatePreview()
            updateCreateTemplateButtonVisibility()
        }
        numberOfRows.addTextChangedListener {
            updatePreview()
            updateCreateTemplateButtonVisibility()
        }

        createTemplateButton.setOnClickListener {
            addTemplateToViewModel()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference to avoid memory leaks
    }

    private fun updatePreview() {
        val numPlayers = numberOfPlayers.text.toString().toIntOrNull() ?: 0
        val numRows = numberOfRows.text.toString().toIntOrNull() ?: 0
        val maxColumnsPerRow = 1 // Adjust this according to your preference

        // Clear previous preview content
        previewLayout.removeAllViews()

        // Generate headers and rows
        for (i in 0 until numPlayers step maxColumnsPerRow) {
            val headerLayout = LinearLayout(requireContext())
            headerLayout.orientation = LinearLayout.HORIZONTAL
            var layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            headerLayout.layoutParams = layoutParams

            // Add headers for this row
            for (j in i until minOf(i + maxColumnsPerRow, numPlayers)) {
                headerLayout.addView(TextView(requireContext()).apply {
                    text = "Player ${j + 1}"
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                })
            }

            previewLayout.addView(headerLayout)

            // Add rows for this header
            for (k in 0 until numRows) {
                val rowLayout = LinearLayout(requireContext())
                rowLayout.orientation = LinearLayout.HORIZONTAL
                val rowLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                rowLayout.layoutParams = rowLayoutParams

                // Add EditText for row name
                rowLayout.addView(EditText(requireContext()).apply {
                    hint = "Row ${k + 1}"
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                })

                // Add EditText fields for player scores
                for (j in i until minOf(i + maxColumnsPerRow, numPlayers)) {
                    rowLayout.addView(EditText(requireContext()).apply {
                        hint = "Enter score"
                        setText((0).toString())
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        ).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    })
                }

                previewLayout.addView(rowLayout)
            }
        }
    }


    private fun updateCreateTemplateButtonVisibility() {
        val numPlayers = numberOfPlayers.text.toString().isNotBlank()
        val numRow = numberOfRows.text.toString().isNotBlank()
        createTemplateButton.visibility = if (numPlayers && numRow) View.VISIBLE else View.GONE
    }

    private fun addTemplateToViewModel() {
        val title = templateTitle.text.toString()
        val gameType = gameTypeText.text.toString()
        val numPlayers = numberOfPlayers.text.toString().toIntOrNull() ?: 0
        val numRows = numberOfRows.text.toString().toIntOrNull() ?: 0

        val rowTitles = mutableListOf<String>()
        //  val firstPlayerLayout = previewLayout.getChildAt(0) as? LinearLayout
        for (i in 0 until numRows+1) {
            val playerLayout = previewLayout.getChildAt(i) as? LinearLayout
            playerLayout?.let { layout ->
                // Skip the first child as it's the header layout
                for (j in 0 until 1) {
                    val editText = layout.getChildAt(j) as? EditText
                    editText?.let {
                        val text = it.text?.toString() ?: ""

                        val rowName = text
                        Log.d("RowTitleDebug", "Row name extracted: $rowName")
                        rowTitles.add(rowName)

                    }
                }
            }
        }


        //
        /*  firstPlayerLayout?.let { layout ->
            for (i in 1 until layout.childCount) { // Start from index 1 to skip the EditText for row name
                val editText = layout.getChildAt(i) as? EditText
                editText?.let {
                    val rowTitle = it.text.toString()
                    Log.d("RowTitleDebug", "Row title extracted: $rowTitle")
                    rowTitles.add(rowTitle)
                }
            }
        }


       */
        // Create a Template object
        val template = Template(
            gameName = title,
            maxPlayers = numPlayers,
            scoreType = gameType,
            rows = numRows,
            rowTitles = rowTitles.take(numRows) // You need to define this based on your logic
        )

        println(template.gameName)
        println(template.maxPlayers)
        println(template.scoreType)
        println(template.rows)
        println(template.rowTitles)

        // Add the template to the shared view model
        println(sharedViewModel.getNumberOfTemplates())
        sharedViewModel.addNewTemplate(template)
        println(sharedViewModel.getNumberOfTemplates())

        val message = "Template added: $title"
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_GameList)

    }
}





/*


private fun updatePreview() {
    val numPlayers = numberOfPlayers.text.toString().toIntOrNull() ?: 0
    val numRows = numberOfRows.text.toString().toIntOrNull() ?: 0
    val maxColumnsPerRow = 1 // Adjust this according to your preference

    if (numPlayers <= 0 || numRows <= 0) {
        // Handle the case where either numPlayers or numRows is 0
        return
    }
    // Clear previous preview content
    previewLayout.removeAllViews()

    // Generate headers and rows
    rowHeaders = Array(numRows) { arrayOfNulls<EditText>(numPlayers) }

    for (i in 0 until numPlayers step maxColumnsPerRow) {
        val headerLayout = LinearLayout(requireContext())
        headerLayout.orientation = LinearLayout.HORIZONTAL
        var layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        headerLayout.layoutParams = layoutParams

        for (j in i until minOf(i + maxColumnsPerRow, numPlayers)) {
            val header = TextView(requireContext()).apply {
                hint = "Player ${j + 1}"
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }
            headerLayout.addView(header)
        }

        previewLayout.addView(headerLayout)

        // Add rows for this header
        for (k in 0 until numRows) {
            val rowLayout = LinearLayout(requireContext())
            rowLayout.orientation = LinearLayout.HORIZONTAL
            val rowLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            rowLayout.layoutParams = rowLayoutParams

            // Add EditText for row name
            val rowHeader = EditText(requireContext()).apply {
                hint = "Row ${k + 1}"
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                // Store the EditText field in the rowHeaders array
                rowHeaders[k][0] = this

            }
            rowLayout.addView(rowHeader)

            // Add EditText fields for player scores
            for (j in i until minOf(i + maxColumnsPerRow, numPlayers)) {
                rowLayout.addView(EditText(requireContext()).apply {
                    hint = "Enter score"
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                })
            }

            previewLayout.addView(rowLayout)
        }
    }
    updateRowHeadersAsync()
}


private fun updateRowHeadersAsync() {
    GlobalScope.launch(Dispatchers.Default) {
        // Perform heavy computation or data manipulation here
        // For example, updating row headers
        for (k in rowHeaders.indices) {
            val newValue = "New value for Row ${k + 1}" // Example new value
            updateRowHeaders(newValue, k, rowHeaders)
        }
    }
}

private fun updateRowHeaders(newValue: String, row: Int, rowHeaders: Array<Array<EditText?>>) {
    // Update the header for the current row
    rowHeaders[row][0]?.let { currentHeader ->
        // Remove the text change listener
        currentHeader.removeTextChangedListener(textWatcher)

        // Set the text without triggering the listener
        currentHeader.setText(newValue)

        // Re-add the text change listener
        currentHeader.addTextChangedListener(textWatcher)
    }

    // Update the headers for all equivalent rows
    for (i in rowHeaders.indices) {
        if (i != row) {
            rowHeaders[i][0]?.let { equivalentHeader ->
                // Remove the text change listener
                equivalentHeader.removeTextChangedListener(textWatcher)

                // Set the text without triggering the listener
                equivalentHeader.setText(newValue)

                // Re-add the text change listener
                equivalentHeader.addTextChangedListener(textWatcher)
            }
        }
    }
}


private val textWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        // Handle text changes here if needed
    }
}

*/