package controller;

import java.util.concurrent.Semaphore;

public class ThreadCorrida extends Thread{
	private int idCarro;
	private int idEscuderia;
	private static int classificados = 0;
	private static double [][] grid = new double [14][3];
	Semaphore entradaPista;
	Semaphore acessoGrid = new Semaphore(1);

	public ThreadCorrida(int idCarro, int idEscuderia, Semaphore entradaPista) {
		this.idCarro = idCarro;
		this.idEscuderia = idEscuderia;
		this.entradaPista = entradaPista;
	}

	@Override
	public void run() {
		do {
			try {
				entradaPista.acquire();
				carroCorrendo();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				entradaPista.release();
				sairPista();
				idCarro++;
				if(classificados == 14) {
					exibeGrid();
				}
			}
		} while (idCarro <= 2);
		
	}

	public void carroCorrendo() {
		double tempoVolta;
		double melhorTempo = 0;
		System.out.println("O carro #"+idCarro+" da Escuderia #"+idEscuderia+" entrou na pista para sua classificatória.");
		for (int idVolta = 1; idVolta <= 3; idVolta ++) {
			tempoVolta = correndoVolta();
			if (melhorTempo == 0) {
				melhorTempo = tempoVolta;
			}else if (tempoVolta < melhorTempo) {
				melhorTempo = tempoVolta;
			}
			System.out.println("O carro #"+idCarro+" da Escuderia #"+idEscuderia+" completou sua "+idVolta+"ª em "+tempoVolta+" minutos.");
		}
		System.out.println("O carro #"+idCarro+" da Escuderia #"+idEscuderia+" fez sua melhor volta em "+melhorTempo+" minutos.");
		gravaGrid(melhorTempo);
	}

	private double correndoVolta() {
		int distanciaPercurso = 4309;
		int contador = 0;
		double velocidadeMedia = 0;
		double velocidade = 0;
		double deslocamento = 0;
		int tempo = 1000;
		while (deslocamento < distanciaPercurso) {
			velocidade = (((double)Math.random() * 17) + 57);
			if (velocidade > (distanciaPercurso - deslocamento)) {
				deslocamento = 4309;
			} else {
				deslocamento += velocidade;
			}
			System.out.println("O carro #"+idCarro+" da Escuderia #"+idEscuderia+" correu "+deslocamento+" metros.");
			try {
				sleep(tempo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			contador++;
			velocidadeMedia += velocidade;
		}
		velocidadeMedia = (velocidadeMedia / contador);
		double tempoVolta = ((distanciaPercurso / velocidadeMedia) / 60);
		return tempoVolta;
	}

	private void gravaGrid(double melhorTempo) {
		int linha = 0;
		int coluna = 0;
		if(idCarro == 1) {
			linha = (idEscuderia - 1);
		}else if (idCarro == 2) {
			linha = (idEscuderia + 6);
		}
		grid[linha][coluna] = idCarro;
		grid[linha][coluna + 1] = idEscuderia;
		grid[linha][coluna + 2] = melhorTempo;
	}

	public void sairPista() {
		System.out.println("O carro #"+idCarro+" da Escuderia #"+idEscuderia+" saiu da pista.");
		classificados++;
	}

	public static void exibeGrid() {
		ordenaGrid(grid);
		System.out.println("\n\n");
		System.out.println("#################################################################################");
		System.out.println("                                      GRID                                       ");
		System.out.println("#################################################################################");
		System.out.println("\n\n");
		for(int linha = 0; linha <= 13; linha++) {
			System.out.println("O Carro #"+grid[linha][0]+" da Escuderia #"+grid[linha][1]+" ficou em "+(linha+1)+"º lugar com uma volta em "+grid[linha][2]+" minutos.");

		}

	}
	private static double[][] ordenaGrid(double[][] grid) {
		double auxiliar [] = new double[3];

		for (int linha = 0; linha <= 12; linha++) {
			for (int linhaAuxiliar = (linha + 1); linhaAuxiliar <= 13; linhaAuxiliar++ ) {
				if (grid[linhaAuxiliar][2] < grid[linha][2]) {
					for(int coluna = 0; coluna <= 2; coluna++) {
						auxiliar[coluna] = grid [linhaAuxiliar][coluna];
						grid[linhaAuxiliar][coluna] = grid[linha][coluna];
						grid[linha][coluna] = auxiliar[coluna];
					}
				}
			}
		}
		return grid;
	}

}
