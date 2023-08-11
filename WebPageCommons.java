import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebPageCommons extends WebPageBase {

	public WebPageCommons(WebDriver driver) {
		WebPageBase.driver = driver;
	}

	String mensagemDeErro = "";
	private static final long DOIS = 2;
	private static final long CINCO = 5;
	private boolean naoPrecisaTratarErro;

	public void executarFuncao(String funcao) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(funcao);
		} catch (Exception e) {

		}
	}
	
	public void executarFuncao(String funcao, WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(funcao, element);
		} catch (Exception e) {

		}
	}

	public void irTelaInicial() {
		driver.navigate().refresh();
		driver.switchTo().defaultContent();
	}

	public void click(By t) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, DOIS);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(t));

			WebElement findElement = driver.findElement(t);
			findElement.click();
		} catch (TimeoutException e) {

		}
	}

	public void confirmaAlerta() {

		try {
			Alert alert = getAlert();
			if (alert != null) {
				alert.accept();
			}
			Thread.sleep(1000);

		} catch (Exception e) {

		}
	}

	public void confirmaNovoAlerta() {
		executarFuncao("esconderAlertaLicencaEsocial()");
	}

	public void confirmaNovoAlertaUsuarioCriado() {
		esperarCarregarElementos();
		executarFuncao("javascript:fechar('avisoAdm')");
	}

	public void cancelaAlerta() {

		if (driver instanceof ChromeDriver) {
			if (getAlert() != null) {
				try {
					WebDriverWait wait = new WebDriverWait(driver, DOIS);
					wait.until(ExpectedConditions.alertIsPresent());
					driver.switchTo().alert().dismiss();
				} catch (Exception e) {

				}
			}
		}

	}

	public void esperarCarregarElementos() {
		try {
			driver.manage().timeouts().implicitlyWait(CINCO, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
	}

	/*
	 * public boolean carregouTodaTela() { esperarCarregarElementos(); return
	 * driver.getPageSource().contains(getBarraInferior()); }
	 */

	public void esperarPeloElemento(By elemento) {
		try {
			new WebDriverWait(driver, 20, 1000).until(ExpectedConditions.presenceOfElementLocated(elemento));
		} catch (Exception e) {
			StringBuilder msg = new StringBuilder();
			mensagemDeErro = "Demorou mais de 20s para encontrar o elemento ".concat(elemento.toString());
			msg.append("\n################################################\n");
			msg.append(mensagemDeErro);
			msg.append("\n################################################");

		}
	}
	
	public void fechaJanelaERetornaDriverJanelaPrincipal() {
		try {
			 Object[] janelas = driver.getWindowHandles().toArray();
			 driver.close();
			 driver.switchTo().window(janelas[0].toString());
		} catch (Exception e) {
			
		}
	}

	public void irParaNovaJanela() {
		try {
			Object[] janelas = driver.getWindowHandles().toArray();
			driver.switchTo().window(janelas[1].toString());
		} catch (Exception e) {

		}
	}
	
	public void preencherCampo(By id, String texto) {
		procurarElemento(id).clear();
		procurarElemento(id).sendKeys(texto);
	}

	public void irParaJanelaPrincipal() {

		Object[] janelas = driver.getWindowHandles().toArray();
		// driver.close();
		driver.switchTo().window(janelas[0].toString());
		irParaFrameConteudo();
	}
	
	public void irParaJanelaPrincipalMaestro() {

		Object[] janelas = driver.getWindowHandles().toArray();
		// driver.close();
		driver.switchTo().window(janelas[0].toString());
		irParaFrameConteudoMaestro();
	}

	public WebElement procurarElemento(By t) {
		esperarPeloElemento(t);
		return driver.findElement(t);
	}

	public List<WebElement> procurarElementos(By t) {
		esperarPeloElemento(t);
		return driver.findElements(t);
	}

	public void selecionarOpcaoComboPeloValor(Select select, String valor) {
		try {
			select.selectByValue(valor);
		} catch (NoSuchElementException e) {
			mensagemDeErro = "Opção não encontrada no Combo: ".concat(valor);
		}
	}

	public void selecionarOpcaoComboPeloTexto(Select select, String texto) {
		try {
			select.selectByVisibleText(texto);
		} catch (NoSuchElementException e) {
			mensagemDeErro = "Opção não encontrada no Combo: ".concat(texto);
		}
	}

	public void selecionarOpcaoComboPeloIndex(Select select, int index) {
		try {
			select.selectByIndex(index);
		} catch (NoSuchElementException e) {

		}
	}

	public void selecionarOpcaoComboPeloJavascript(By elemento, String txt) {
		WebElement dropDownListBox = driver.findElement(elemento);

		((JavascriptExecutor) driver).executeScript(
				"var select = arguments[0]; for(var i = 0; i < select.options.length; i++)"
						+ "{ if(select.options[i].text == arguments[1]){ select.options[i].selected = true; } }",
				dropDownListBox, txt);
	}

	public void esperarCarregarBarraInferior() {
		try {

			new WebDriverWait(driver, CINCO)
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("barraInferior")));
		} catch (Exception e) {
			mensagemDeErro = "Não carregou a Barra inferior";
			Assert.fail(mensagemDeErro);
		}
	}

	public Alert getAlert() {
		try {
			return driver.switchTo().alert();
		} catch (NoAlertPresentException e) {
		}
		return null;
	}

	public void irParaFrameSuperior() {
		driver.switchTo().defaultContent();
	}

	/*
	 * public boolean isBotaoEncontradoNaTela(String nomeBotao) { boolean
	 * achouBotaoNaTela = false; esperarPeloElemento(By.id(getDivBotes()));
	 * List<WebElement> botoesList =
	 * driver.findElement(By.id(getDivBotes())).findElements(By.tagName("a"));
	 * for (WebElement botao : botoesList) { if
	 * (botao.getAttribute("href").contains(nomeBotao)) { achouBotaoNaTela =
	 * true; break; } } return achouBotaoNaTela; }
	 */

	public boolean isElementoVisivel(By locatorElemento) {
		try {
			driver.findElement(locatorElemento);
			return true;
		} catch (NoSuchElementException exception) {
			return false;
		}
	}
	
	public boolean isElementoInvisivel(By locatorElemento) {
		try {
			driver.findElement(locatorElemento);
			return false;
		} catch (NoSuchElementException exception) {
			return true;
		}
	}

	public String pegarTextoDoElemento(By locator) {
		return procurarElemento(locator).getText().trim().replaceAll("/n", "");
	}

	public void manterCursorDoMouseNoElemento(By locator) {
		Actions actions = new Actions(driver);
		actions.clickAndHold(procurarElemento(locator)).perform();
	}

	public void esperarPeloAlerta() {
		try {
			new WebDriverWait(driver, 10).until(ExpectedConditions.alertIsPresent());
		} catch (TimeoutException e) {
			mensagemDeErro = "ALERTA NÃO FOI EXIBIDO EM 10 SEGUNDOS";
			Assert.fail(mensagemDeErro);
		}
	}

	public void esperarPelaVisibilidadeDosElementos(By... locator) {
		try {
			for (By by : locator) {
				new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
			}
		} catch (Exception e) {
			mensagemDeErro = " ERRO ELEMENTO " + locator + " NÃO FICOU VISÍVEL EM 30 SEGUNDOS";
			Assert.fail(mensagemDeErro);
		}
	}

	public void esperarParaQueOElementoEstejaClicavel(WebElement dataLicencaInicio) {
		try {
			new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(dataLicencaInicio));
		} catch (Exception e) {
			mensagemDeErro = " ERRO ELEMENTO " + dataLicencaInicio + " NÃO FICOU VISÍVEL EM 10 SEGUNDOS ";
			Assert.fail(mensagemDeErro);
		}
	}

	public void esperarParaQueElementoEstejaInvisivel(By locator) {
		try {
			new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(locator));
		} catch (Exception e) {
			mensagemDeErro = " ERRO ELEMENTO " + locator + " NÃO FICOU INVISIVEL EM 10 SEGUNDOS ";
			Assert.fail(mensagemDeErro);
		}
	}

	protected boolean isElementoEnable(By locator) {
		return procurarElemento(locator).isEnabled();
	}

	private List<WebElement> getTodosBotoes() {
		return driver.findElements(By.tagName("a"));
	}

	public void executarScriptComParteDoNomeDoLink(String script) {
		esperarCarregarElementos();
		List<WebElement> botoesList = this.getTodosBotoes();
		for (WebElement botao : botoesList) {
			if (botao.getAttribute("href") != null && botao.getAttribute("href").contains(script)) {
				botao.click();
				break;
			}
		}
	}

	public void irParaFrameConteudo() {
		WebElement iframe;
		iframe = driver.findElement(By.id("frame"));
		driver.switchTo().frame(iframe);
	}

	public void irParaFrameConteudoMaestro() {
		WebElement iframe;
		iframe = driver.findElement(By.id("maestroframe"));
		driver.switchTo().frame(iframe);
	}

	public void irParaFrameConteudoMaestroMenu() {
		WebElement iframe;
		iframe = driver.findElement(By.id("menu_iframe"));
		driver.switchTo().frame(iframe);
	}

	public void irParaFramePendencia() {
		WebElement iframe;
		iframe = driver.findElement(By.id("win2"));
		driver.switchTo().frame(iframe);
	}

	public void irParaFrameUpload() {
		WebElement iframe;
		iframe = driver.findElement(By.id("frameArquivosForm"));
		driver.switchTo().frame(iframe);
	}
	
	public void irParaFrameUpload2() {
		WebElement iframe;
		iframe = driver.findElement(By.id("frameArquivos"));
		driver.switchTo().frame(iframe);
	}

	public void esperarTerminarDeCarregar() {

		irParaFrameSuperior();
		WebElement spanCarregando = procurarElemento(By.id("divCarregandoSub"));

		if (spanCarregando.isDisplayed()) {
			esperarTerminarDeCarregar();
			// return esperarTerminarDeCarregar();
		}

		irParaFrameConteudo();
		// return this;
	}

	public boolean gerouPedProc() {
		return this.isPossuiTodosDadosNaTela("mensagemProc", "Código de Identificação do Pedido : ");
	}

	public boolean isPossuiTodosDadosNaTela(CharSequence... campos) {
		String conteudoDaPagina = driver.getPageSource();
		esperarCarregarElementos();
		boolean possuiTodosOsCampos = true;
		for (CharSequence campo : campos) {
			if (!conteudoDaPagina.contains(campo)) {
				possuiTodosOsCampos = false;
				break;
			}
		}
		return possuiTodosOsCampos;
	}

	public void clicarCodigoParaConsulta() {
		esperarCarregarElementos();
		procurarElemento(By.className("linkln")).click();
		esperarCarregarElementos();
	}

	protected void executarAcaoIconeOculto(String funcao) {

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

	/**
	 * Limpa a mascara que existe no campo de preenchimento e escreve texto
	 * neste mesmo campo. e necessario indicar o id referente. Exemplo:
	 * 'escreveTextoId("elementoId") , "texto")'
	 */
	public void escreveTextoId(By campousuario, String texto) {
		getDriver().findElement(campousuario).clear();
		getDriver().findElement(campousuario).sendKeys(texto);
	}
	
	public void escreveTextoId(By campousuario, Keys texto) {
		getDriver().findElement(campousuario).clear();
		getDriver().findElement(campousuario).sendKeys(texto);
		
	}

	/**
	 * Encontra campo de preenchimento por Name, escreve texto neste mesmo
	 * campo. Exemplo: 'escreveTextoName("elementoName" , "texto")'
	 * 
	 */
	public void escreveTextoName(By camponome, String texto) {

		getDriver().findElement(camponome).sendKeys(texto);
	}

	public void limparcampo(By camponome) {

		getDriver().findElement(camponome).clear();
		;
	}

	/**
	 * Encontra elemento e confirma se esta selecionado. Exemplo:
	 * 'isElementoMarcadoId("Id")'
	 */
	public Boolean isElementoMarcadoId(String id) {
		return getDriver().findElement(By.id(id)).isSelected();
	}

	/**
	 * Encontra elemento e confirma se esta selecionado. Exemplo:
	 * 'isElementoMarcadoName("name")'
	 */
	public Boolean isElementoMarcadoName(String name) {
		return getDriver().findElement(By.name(name)).isSelected();
	}

	/**
	 * Encontra elemento e confirma se esta selecionado. Exemplo:
	 * 'isElementoMarcado("className")'
	 */
	public Boolean isElementoMarcadoClassName(String className) {
		return getDriver().findElement(By.className(className)).isSelected();
	}

	/**
	 * Encontra campo Combo e seleciona elemento por texto deste Combo. Exemplo:
	 * 'selecionarCombo("Id" , "texto")'
	 */
	public void selecionarComboId(String id, String texto) {
		WebElement elemento = getDriver().findElement(By.id(id));
		Select combo = new Select(elemento);
		combo.selectByVisibleText(texto);
	}
	
	public void selecionarComboIdPorValue(String id, String texto) {
		WebElement elemento = getDriver().findElement(By.id(id));
		Select combo = new Select(elemento);
		combo.selectByValue(texto);
	}

	/**
	 * Encontra campo Combo e seleciona elemento por texto deste Combo. Exemplo:
	 * 'selecionarComboName("name" , "texto")'
	 */
	public void selecionarComboName(String name, String texto) {
		WebElement elemento = getDriver().findElement(By.name(name));
		Select combo = new Select(elemento);
		combo.selectByVisibleText(texto);
	}

	/**
	 * Encontra campo Combo e seleciona elemento por texto deste Combo. Exemplo:
	 * 'selecionarComboClassName("className" , "TextoDoCombo")'
	 */
	public void selecionarComboClassName(String className, String texto) {
		WebElement elemento = getDriver().findElement(By.className(className));
		Select combo = new Select(elemento);
		combo.selectByVisibleText(texto);
	}

	/**
	 * Encontra elemento pela indicacao solicitada e clica.
	 */
	public void clicaElementoId(By id) {
		getDriver().findElement(id).click();
	}

	/**
	 * Encontra elemento pela indicacao solicitada e clica.
	 */
	public void clicaElementoName(By name) {
		getDriver().findElement(name).click();
	}

	/**
	 * Encontra elemento pela indicacao solicitada e clica.
	 */
	public void clicaElementoLink(String linkText) {
		getDriver().findElement(By.linkText(linkText)).click();
	}

	/**
	 * Encontra elemento pela indicacao solicitada e clica.
	 */
	public void clicaElementoClassName(String className) {
		getDriver().findElement(By.className(className)).click();
	}

	/**
	 * Encontra elemento pela indicacao solicitada e clica.
	 * 
	 * @param xpath
	 */
	public void clicaElementoXPath(By xpath) {
		getDriver().findElement(xpath).click();
	}
	
	public void esperarParaQueOElementoEstejaClicavelBy(By dataLicencaInicio) {
		try {
			new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(dataLicencaInicio));
		} catch (Exception e) {
			mensagemDeErro = " ERRO ELEMENTO " + dataLicencaInicio + " NÃO FICOU VISÍVEL EM 10 SEGUNDOS ";
			Assert.fail(mensagemDeErro);
		}
	}
	
	public int pegarQuantidadeCampos(By Campos) {
		int myCount = driver.findElements(Campos).size();
		return myCount;
	}
	
	public void doubleClickNoElemento(By elemento) {
		Actions actions = new Actions(driver);
		WebElement elementLocator = driver.findElement(elemento);
		actions.doubleClick(elementLocator).perform();
	}
	
	public void verificarValorElemento(String atual, By elemento) {
		Assert.assertEquals(atual, pegarTextoDoElemento(elemento));
	}
	
	public void verificarValorElementoString(String atual, String elemento) {
		Assert.assertEquals(atual, elemento);
	}
	
	/*highlight*/
	public boolean highlightElement(WebDriver driver, WebElement element) {
		try {
			if (driver instanceof JavascriptExecutor) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].style.border='3px solid red'", element);
				driver = null;
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return false;
	}
	  
	/*unhighlight*/
	  public boolean UnhighlightElement(WebDriver driver, WebElement element) {
        try {
               if (driver instanceof JavascriptExecutor) {
                     JavascriptExecutor js = (JavascriptExecutor) driver;
                     js.executeScript("arguments[0].style.border=''", element, "");
                     driver = null;
               }
        } catch (Exception e) {
               e.getMessage();
        }
		return false;
 }

	
}