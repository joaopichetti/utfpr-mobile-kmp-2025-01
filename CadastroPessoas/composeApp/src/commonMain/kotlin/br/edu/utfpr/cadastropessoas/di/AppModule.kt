package br.edu.utfpr.cadastropessoas.di

import br.edu.utfpr.cadastropessoas.data.datasource.PessoaDatasource
import br.edu.utfpr.cadastropessoas.data.datasource.driver.getDriverFactory
import br.edu.utfpr.cadastropessoas.data.repository.CepRepository
import br.edu.utfpr.cadastropessoas.data.repository.PessoaRepository
import br.edu.utfpr.cadastropessoas.ui.detalhes.DetalhesPessoaViewModel
import br.edu.utfpr.cadastropessoas.ui.form.FormPessoaViewModel
import br.edu.utfpr.cadastropessoas.ui.lista.ListaPessoasViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule: Module = module {
    factory { getDriverFactory(this) }
    singleOf(::CepRepository)
    singleOf(::PessoaDatasource)
    singleOf(::PessoaRepository)
    viewModelOf(::ListaPessoasViewModel)
    viewModelOf(::DetalhesPessoaViewModel)
    viewModelOf(::FormPessoaViewModel)
}