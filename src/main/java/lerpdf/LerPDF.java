package lerpdf;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormat;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import lerpdf.model.Dados;

public class LerPDF {

	private static final String REGEX_LINHA_COM_DATA = "^(\\d{2}/\\d{2}/\\d{4})(.*?)";
	private static List<Dados> listaDeDados = null;

	public static void main(String[] args) {

		try {
			PdfReader pdf = new PdfReader(new FileInputStream("extrato.pdf"));
			Pattern padrao = Pattern.compile(REGEX_LINHA_COM_DATA);
			for (int i = 1; i <= pdf.getNumberOfPages(); i++) {
				String[] linhas = PdfTextExtractor.getTextFromPage(pdf, i).split("\n");
				verificaSeTemData(padrao, linhas);
			}

			getListaDeDados().forEach(new Consumer<Dados>() {
				public void accept(Dados d) {
					System.out.println(d);
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void verificaSeTemData(Pattern padrao, String[] linhas) {
		for (String l : linhas) {
			Matcher compara = padrao.matcher(l);
			if (compara.matches()) {
				// System.out.println("Esta linha contém uma data: " + l);
				popularDado(compara);
			}
		}
	}

	private static void popularDado(Matcher m) {
		Dados dados = new Dados();
		dados.setData(obterDataLancamento(m.group(1)));
		dados.setDescricao(obterDescricaoDetalhada(m.group(2)));
		dados.setNumerodocumento(obterNumeroDocumento(m.group(2)));
		dados.setValor(obterValorLancamento(m.group(2)));
		getListaDeDados().add(dados);
	}

	private static Date obterDataLancamento(String dataString) {
		try {
			return DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(dataString).toDate();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String obterDescricaoDetalhada(String descricao) {
		String descricaoLancamento = descricao.replaceAll("[\\d|\\W&&\\S]", "").trim();
		return descricaoLancamento.length() > 100 ? descricaoLancamento.substring(0, 100) : descricaoLancamento;
	}

	private static String obterNumeroDocumento(String linha) {
		String campoSemDescricaoLancamento[] = linha.replaceAll("[^\\d&&\\S]", "").split(" ");
		return campoSemDescricaoLancamento[1].matches("(\\d{2}/\\d{2}/\\d{4})") ? campoSemDescricaoLancamento[3]
				: campoSemDescricaoLancamento[2];
	}

	private static Double obterValorLancamento(String valor) {
		String valorString[] = valor.replaceAll("[^\\d&&\\S]", "").split(" ");
		Double valorLancamento = Double.valueOf(valorString[valorString.length - 1]);
		if (valor.contains("-")) {
			valorLancamento = valorLancamento * -1;
		}
		return valorLancamento / 100;
	}

	public static List<Dados> getListaDeDados() {
		if (listaDeDados == null) {
			listaDeDados = new ArrayList<Dados>();
		}
		return listaDeDados;
	}

}
