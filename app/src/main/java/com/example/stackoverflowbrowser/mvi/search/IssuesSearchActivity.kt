package com.example.stackoverflowbrowser.mvi.search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser._base.mvi.MviView
import com.example.stackoverflowbrowser.mvi.search.list.IssuesAdapter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel

class IssuesSearchActivity : MviView<IssuesSearchIntent, IssuesSearchViewState>, AppCompatActivity() {

    private val viewModel: IssuesSearchViewModel by viewModel()
    private val issuesAdapter = IssuesAdapter()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.states.subscribe(::render).addTo(disposables)
        viewModel.processIntents(intents)
    }

    override fun onStop() {
        disposables.clear()
        super.onStop()
    }

    private fun initRecyclerView() {
        issueList.adapter = issuesAdapter
        issueList.layoutManager = LinearLayoutManager(this)
    }

    override val intents: Observable<IssuesSearchIntent>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun render(state: IssuesSearchViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
