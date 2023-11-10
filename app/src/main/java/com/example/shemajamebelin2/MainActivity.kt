package com.example.shemajamebelin2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shemajamebelin2.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val wordList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            wordList.addAll(savedInstanceState.getStringArrayList("wordList") ?: emptyList())
        }

        binding.btnSave.setOnClickListener {
            val word = binding.etEnterAnagram.text.toString().trim()

            if (word.isNotBlank() && isWordValid(word)) {
                wordList.add(word)
                binding.etEnterAnagram.text?.clear()
                showSuccessSnackbar()
            } else {
                showErrorSnackbar()
            }
        }

        binding.btnOutput.setOnClickListener {
            val anagramGroups = groupAnagrams(wordList)
            val anagramCount = anagramGroups.size
            val anagramResult = buildAnagramResultString(anagramGroups)

            binding.tvAnagramCount.text = "Anagrams count: $anagramCount"
            binding.tvAnagramResult.text = anagramResult
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("wordList", ArrayList(wordList))
    }

    private fun groupAnagrams(words: List<String>): Map<String, List<String>> {
        return words.groupBy { it.toCharArray().sorted().joinToString("") }
    }

    private fun buildAnagramResultString(anagramGroups: Map<String, List<String>>): String {
        val result = StringBuilder()
        for ((key, anagrams) in anagramGroups) {
            result.append("Anagrams for '$key':\n")
            result.append(anagrams.joinToString(", "))
            result.append("\n\n")
        }
        return result.toString()
    }

    private fun isWordValid(word: String): Boolean {
        val invalidSymbols = setOf('/', '?', '!', '"', '.','@', '#', '$', '%', '^', '&', '*','0','1','2','3','4','5','6','7','8','9')
        return !word.any {
            it in invalidSymbols
        }
    }

    private fun showSuccessSnackbar() {
        Snackbar.make(binding.root, "Word Saved", Snackbar.LENGTH_SHORT).show()
    }

    private fun showErrorSnackbar() {
        Snackbar.make(binding.root, "Enter word correctly", Snackbar.LENGTH_SHORT)
            .show()
    }

}