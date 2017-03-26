package lerpdf.model;

import java.util.Date;

public class Dados {

	private Date data;
	private String descricao;
	private String numerodocumento;
	private double valor;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNumerodocumento() {
		return numerodocumento;
	}

	public void setNumerodocumento(String numerodocumento) {
		this.numerodocumento = numerodocumento;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "Dados [data=" + data + ", descricao=" + descricao + ", numerodocumento=" + numerodocumento + ", valor="
				+ valor + "]";
	}

}
