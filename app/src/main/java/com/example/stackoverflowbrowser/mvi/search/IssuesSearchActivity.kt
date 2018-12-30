package com.example.stackoverflowbrowser.mvi.search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.stackoverflowbrowser.R
import com.example.stackoverflowbrowser._base.mvi.MviView
import io.reactivex.Observable

class IssuesSearchActivity : MviView<IssuesSearchIntent, IssuesSearchViewState>, AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    override val intents: Observable<IssuesSearchIntent>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun render(state: IssuesSearchViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
