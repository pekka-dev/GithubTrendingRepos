package com.example.githubtrendingrepos.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.example.githubtrendingrepos.R
import com.example.githubtrendingrepos.adapter.ReposListAdapter
import com.example.githubtrendingrepos.adapter.ReposLoadStateFooterAdapter
import com.example.githubtrendingrepos.databinding.ActivityMainBinding
import com.example.githubtrendingrepos.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

	private val viewModel by viewModels<MainViewModel>()
	private var _binding: ActivityMainBinding? = null
	private val binding get() = _binding!!
	private var queryString: String = MainViewModel.DEFAULT_QUERY

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		_binding = ActivityMainBinding.bind(
			((findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup)
		)

		val adapter = ReposListAdapter(this.applicationContext)

		binding.apply {
			recyclerView.setHasFixedSize(false)
			recyclerView.adapter =
				adapter.withLoadStateHeaderAndFooter(
					header = ReposLoadStateFooterAdapter { adapter.retry() },
					footer = ReposLoadStateFooterAdapter { adapter.retry() }
				)
			recyclerView.clearAnimation()
			swipeToRefreshLayout.setOnRefreshListener {
				viewModel.searchQuery(queryString)
				adapter.notifyDataSetChanged()
				swipeToRefreshLayout.isRefreshing = false
			}

			retryButton.setOnClickListener {
				adapter.retry()
			}
		}

		adapter.addLoadStateListener { loadState ->
			binding.apply {
				progressBar.isVisible = loadState.source.refresh is LoadState.Loading
				recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
				retryLayout.isVisible = loadState.source.refresh is LoadState.Error

				if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
					recyclerView.isVisible = false
					noRepoFoundError.isVisible = true
				} else {
					noRepoFoundError.isVisible = false
				}
			}
		}

		viewModel.repos.observe(this) {
			adapter.submitData(this.lifecycle, it)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater: MenuInflater = menuInflater
		inflater.inflate(R.menu.menu_items, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.date_picker -> {
				val calendar = Calendar.getInstance()
				val year = calendar.get(Calendar.YEAR)
				val month = calendar.get(Calendar.MONTH)
				val day = calendar.get(Calendar.DAY_OF_MONTH)
				val datePicker = DatePickerDialog(
					this, this, year, month, day
				)
				datePicker.show()
				true
			}
			R.id.refresh -> {
				viewModel.searchQuery(queryString)
				binding.recyclerView.adapter?.notifyDataSetChanged()
				binding.recyclerView.scrollToPosition(0)
				true
			}
			else -> super.onOptionsItemSelected(item)

		}

	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

	override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
		val cMonth = month + 1
		queryString =
			"created:>$year-${if (cMonth > 10) cMonth else "0$cMonth"}-${if (dayOfMonth > 10) dayOfMonth else "0$dayOfMonth"}"
		viewModel.searchQuery(queryString)
		binding.recyclerView.adapter?.notifyDataSetChanged()
		binding.recyclerView.scrollToPosition(0)
	}

}