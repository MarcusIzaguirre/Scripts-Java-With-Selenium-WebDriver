import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebPageMethods extends WebPageBase {

	public static final int ZERO = 0;
	public static final int UM = 1;
	public static final int DOIS = 2;
	private String nomeFuncionario;
	private List<String> exames = new ArrayList<String>();

	WebPage pageElements = new WebPage(driver);

	public WebPageMethods(WebDriver driver) {
		super.driver = driver;
	}

	public boolean campoIndisponivel(String nomeIdCampo) {
		WebElement campo = pageElements.procurarElemento(By.id(nomeIdCampo));
		return !campo.isDisplayed();
	}

	public boolean isPossuiTodosDadosNaTela(CharSequence... campos) {
		String conteudoDaPagina = driver.getPageSource();
		boolean possuiTodosOsCampos = true;
		for (CharSequence campo : campos) {
			if (!conteudoDaPagina.contains(campo)) {
				possuiTodosOsCampos = false;
				break;
			}
		}
		return possuiTodosOsCampos;
	}

	public void pesquisarFuncionario(String nomeFuncionario) {
		WebElement nomeSeach = pageElements.procurarElemento(By.name("nomeSeach"));
		executarFuncao("javascript:todos();");
		nomeSeach.clear();
		nomeSeach.sendKeys(nomeFuncionario);
		executarBrowser();
		clicarCodigoParaConsulta();

	}

	public void executarBrowser() {
		doAcao(Elements.BROWSER);
	}

	public boolean gerouPedProc() {
		return this.isPossuiTodosDadosNaTela("mensagemProc", "Código de Identificação do Pedido : ");
	}

	public void clicarCodigoEmpresaParaConsulta() {
		pageElements.click(By.className("codigo"));
	}

	public void doAcao(String acao) {
		try {
			pageElements.esperarPeloElemento(By.id("botoes"));
			String acaoExecutar = "javascript:doAcao('".concat(acao).concat("')");
			this.executarFuncao(acaoExecutar);
		} catch (Exception e) {

		}
	}

	public void clicarCodigoParaConsulta() {
		pageElements.esperarPelaVisibilidadeDosElementos(By.className("linkln"));
		pageElements.procurarElemento(By.className("linkln")).click();
	}

	public void executarFuncao(String funcao) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(funcao);
		} catch (Exception e) {

		}

	}

	public Object executarFuncaoRetorno(String funcao) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {

			return js.executeScript(funcao);
		} catch (Exception e) {

		}
		return js;
	}

	public void executarAcaoIconeOculto(String funcao) {
		WebElement botao = driver.findElement(By.id("botaoIconesOcultos"));
		botao.click();

		List<WebElement> listaMaisIcones = driver.findElements(By.className("maisBotoesLi"));

		for (WebElement iconeOculto : listaMaisIcones) {
			if (iconeOculto.getAttribute("onclick").contains(funcao)) {
				iconeOculto.click();
				break;
			}
		}
	}

	public boolean isGravado(CharSequence... campos) {
		boolean achouBotaoAlterar = false;
		pageElements.esperarPeloElemento(By.id("botoes"));

		List<WebElement> botoesList = driver.findElement(By.id("botoes")).findElements(By.tagName("a"));

		for (WebElement botao : botoesList) {
			if (botao.getAttribute("href").contains("javascript:doAcao('alt')")) {
				achouBotaoAlterar = true;
				break;
			}
		}

		return isPossuiTodosDadosNaTela(campos) && achouBotaoAlterar;
	}

	public boolean isElementoVisivel(By locatorElemento) {
		try {
			driver.findElement(locatorElemento);
			return true;
		} catch (NoSuchElementException exception) {

			return false;
		}
	}
	
	public boolean isVerificaMensagemAlerta(String id, String tagName, String mensagemAlerta) {
		pageElements.esperarCarregarElementos();
		WebElement modalAlerta = pageElements.procurarElemento(By.id(id));
		new WebDriverWait(driver, 20, 1000).until(ExpectedConditions.visibilityOf(modalAlerta));
		List<WebElement> alertas = modalAlerta.findElements(By.tagName(tagName));

		for (WebElement alerta : alertas) {
			if (alerta.getText() != null && alerta.getText().contains(mensagemAlerta)) {
				return true;
			}
		}
		return false;
	}

	public void imprimir() {
		doAcao("imprimir");
	}

	public void clicarTrocaEmpresa() {
		pageElements.irParaFrameSuperior();
		pageElements.executarScriptComParteDoNomeDoLink(TROCAR_EMPRESA);
		pageElements.irParaFrameConteudo();
	}

	public void irParaPrograma(String codigoPrograma) {

		pageElements.irParaFrameSuperior();

		WebElement input = driver.findElement(Elements.CAMPO_CODIGO_PROGRAMA);
		WebElement botao = driver.findElement(Elements.BOTAO_OK);

		input.click();
		input.clear();

		input.sendKeys(codigoPrograma);
		botao.click();

		pageElements.irParaFrameConteudo();
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	public String getNomeFuncionario() {
		return UtString.isNullOrEmpty(nomeFuncionario) ? Keys.NOME_FUNCIONARIO : nomeFuncionario;
	}

	public List<String> getExames() {

		if (UtGeral.isNullOrEmptyList(exames)) {
			exames.add("51.01.004-6");
			exames.add("50.01.001-8");
		}

		return exames;
	}

	public void pesquisar(String txtPesquisa) {
		WebElement nomeSeach = driver.findElement(By.name("nomeSeach"));
		nomeSeach.sendKeys(txtPesquisa);
		doAcao("browse");
	}

	// método não possui o doAcao
	public void pesquisar_pri(String txtPesquisa) {
		WebElement nomeSeach = driver.findElement(By.name("nomeSeach"));
		nomeSeach.sendKeys(txtPesquisa);
	}
	
	public boolean isOpcaoEspecificaDisponivelEmCombo(String idCombo, String opcaoProcurada) {
		boolean opcaoEncontrada = false;
		List<WebElement> opcoesCombo = driver.findElement(By.id(idCombo)).findElements(By.tagName("option"));

		for (WebElement opcaoCombo : opcoesCombo) {
			if (opcaoProcurada.equals(opcaoCombo.getText().trim())) {
				opcaoEncontrada = true;
				break;
			}
		}
		return opcaoEncontrada;
	}

	public boolean isOpcoesDisponiveisNoCombo(String idCombo, String... opcoesProcuradas) {
		List<WebElement> opcoesCombo = pageElements.procurarElemento(By.id(idCombo)).findElements(By.tagName("option"));
		List<String> opcoesProcuradasNoSelect = Arrays.asList(opcoesProcuradas);

		for (WebElement option : opcoesCombo) {
			if (!opcoesProcuradasNoSelect.contains(option.getText().trim())
					&& !UtString.isNullOrEmpty(option.getText())) {
				return false;
			}
		}

		return true;
	}

	public boolean isBotaoEncontradoNaTela(String nomeBotao) {
		boolean achouBotaoNaTela = false;
		pageElements.esperarPeloElemento(By.id("botoes"));
		List<WebElement> botoesList = driver.findElement(By.id("botoes")).findElements(By.tagName("a"));
		for (WebElement botao : botoesList) {
			if (botao.getAttribute("href").contains(nomeBotao)) {
				achouBotaoNaTela = true;
				break;
			}
		}
		return achouBotaoNaTela;
	}

	public boolean isUsuarioNoPrograma(String infoPrograma) {
		pageElements.irParaFrameSuperior();
		pageElements.esperarCarregarElementos();
		return pageElements.procurarElemento(By.id("infoPrograma")).getText().trim().equalsIgnoreCase(infoPrograma);
		
	}

	public void trocarEmpresaClientePeloCodigo(String codigoEmpresa) {
		pageElements.esperarCarregarElementos();
		WebElement inputEmpresa = pageElements.procurarElemento(By.id("cproemp"));
		inputEmpresa.clear();
		inputEmpresa.sendKeys(codigoEmpresa);
		pageElements.procurarElemento(By.id("procuraModalBtn")).click();

		By linkEmpresa = By.cssSelector("#listaemop > table > tbody > tr > td.campo2 > a:nth-child(1)");

		if (this.isElementoVisivel(linkEmpresa)) {
			pageElements.procurarElemento(linkEmpresa).click();
		}
	}

	public void trocarEmpresaParaPrincipal() {
		pageElements.irParaFrameSuperior();
		pageElements.click(ICONE_BOTAO_TROCA_EMPRESA);
		pageElements.irParaFrameConteudo();
		pageElements.click(BOTAO_TROCA_EMPRESA_PRINCIPAL);

	}

	public boolean validarValidate(String nomeCampoValidar, String msg) {
		pageElements.esperarCarregarElementos();
		boolean isCampoValidado = false;
		List<WebElement> validates = driver.findElements(By.tagName("span"));
		for (WebElement validate : validates) {
			if (validate.getAttribute("for") != null && validate.getAttribute("for").contains(nomeCampoValidar)) {
				WebElement img = validate.findElement(By.tagName("img"));
				isCampoValidado = img.getAttribute("title").contains(msg);
				break;
			}
		}
		pageElements.esperarCarregarElementos();
		return isCampoValidado;
		
	}

	public void consultarFichaDoDia(String data) {
		List<WebElement> tabelaFichas = pageElements
				.procurarElementos(By.xpath("//*[@id='tabelaFichas']/tbody/tr[position()>1]/td[1]"));
		for (WebElement td : tabelaFichas) {
			if (data.equals(td.getText())) {
				td.findElement(By.tagName("a")).click();
				break;
			}
		}
	}

	public void executarScriptImagem(String funcao) {
		pageElements.esperarCarregarElementos();
		List<WebElement> botoesList = this.getTodosBotoesImagem();

		for (WebElement botao : botoesList) {
			if (botao.getAttribute("src").contains(funcao)) {
				botao.click();
				break;
			}
		}
	}

	private List<WebElement> getTodosBotoesImagem() {
		return driver.findElements(By.tagName("img"));
	}

	// MÉTODOS PARA O ENTRAR EM FRANQUIA SESI

	public void selecionaSesiDR() {
		pageElements.esperarPelaVisibilidadeDosElementos(OPCAO_FRANQUIA_SESI_DR);
		pageElements.procurarElemento(OPCAO_FRANQUIA_SESI_DR).click();
		clicarEntrarFranquiaSESI();
	}

	public void selecionarSesiUo() {
		pageElements.esperarPelaVisibilidadeDosElementos(OPCAO_FRANQUIA_SESI_DR);
		pageElements.procurarElemento(OPCAO_FRANQUIA_SESI_DR).click();
		pageElements.esperarPelaVisibilidadeDosElementos(OPCAO_FRANQUIA_SESI_UO);
		pageElements.procurarElemento(OPCAO_FRANQUIA_SESI_UO).click();
		clicarEntrarFranquiaSESI();
	}

	public void clicarEntrarFranquiaSESI() {
		pageElements.procurarElemento(BOTAO_ENTRAR_FRANQUIA).click();
	}

}