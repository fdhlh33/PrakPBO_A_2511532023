package Pekan1;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Rekening {
	String nomorRekening;
	String namaPemilik;
	double saldo;
	
	public Rekening(String nomor, String nama, double saldoAwal) {
		nomorRekening = nomor;
		namaPemilik = nama;
		saldo = saldoAwal;
		System.out.println("Rekening atas nama " + namaPemilik + " berhasil dibuat dengan saldo Rp " + saldo); 
	}
	
	private String formatRupiah(double nominal) {
		DecimalFormatSymbols simbol = new DecimalFormatSymbols(new Locale("id", "ID"));
		simbol.setGroupingSeparator('.');
		simbol.setDecimalSeparator(',');
		
		DecimalFormat formatter = new DecimalFormat("#,##0.00", simbol);
		return formatter.format(nominal);
	}
	
	public void setorTunai(double nominal) {
		if (nominal > 10000) {
			saldo += nominal;
			System.out.println("Setor tunai Rp " + formatRupiah(nominal) + " berhasil. Saldo saat ini: Rp " + formatRupiah(saldo));
		} else {
			System.out.println("Gagal: Nominal setor harus lebih dari 10.000!");
		}
	}
	
	public void tarikTunai(double nominal) {
		if (nominal < 10000) {
			System.out.println("Transaksi Gagal : Minimal nominal penarikan 10.000");
		} else if (nominal > saldo) {
			System.out.println("Transaksi Gagal: Saldo tidak mencukupi. Saldo Anda: Rp " + formatRupiah(saldo));
		} else {
			saldo -= nominal;
			System.out.println("Transaksi sebesar Rp " + formatRupiah(nominal) + " berhasil! Saldo anda tersisa: Rp " + formatRupiah(saldo));
		}
	}
	
	public void cekInformasi() {
		System.out.println("--- INFO REKENING ---");
		System.out.println("No. Rekening : " + nomorRekening);
		System.out.println("Nama Pemilik : " + namaPemilik);
		System.out.println("Saldo Akhir  : Rp " + formatRupiah(saldo));
		System.out.println("-----------------------");
	}
}
