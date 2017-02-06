/*   
 *   Super Mario Propulsion
 *   Copyright (C) Este programa é software livre; você pode redistribuí-lo e/ou
 *   modificá-lo sob os termos da Licença Pública Geral GNU, conforme
 *   publicada pela Free Software Foundation; tanto a versão 2 da
 *   Licença como (a seu critério) qualquer versão mais nova.
 *
 *   Este programa é distribuído na expectativa de ser útil, mas SEM
 *   QUALQUER GARANTIA; sem mesmo a garantia implícita de
 *   COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM
 *   PARTICULAR. Consulte a Licença Pública Geral GNU para obter mais
 *   detalhes.
 *
 *   Você deve ter recebido uma cópia da Licença Pública Geral GNU
 *   junto com este programa; se não, escreva para a Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *   02111-1307, USA.
 */

package etc;

public class Temporizador
{
	private Animacao origem;

	private final int MAX_PULO_DE_QUADROS = 25;
	private final int NUM_FPS_PARA_MEDIA = 10;
	private final long MAX_INTERVALO_PARA_ESTATISTICAS = 1000000000L;

	private long intervaloParaEstatisticas = 0L; //em nanosegs
	private long tempoDaEstatisticaAnterior;  
	
	private long contadorDeFrames = 0;
	private double gravadorDeFPS[];
	private long contadorDeEstatisticas = 0;
	private double mediaDeFPS = 0.0;

	private long quadrosPulados = 0L;
	private long totalDeQuadrosPulados = 0L;
	private double gravadorDeUPS[];
	private double mediaDeUPS = 0.0;
	
	private long tempoDecorrido = 0L;
	
	private long TEMPO, tempoAntes, tempoDepois, tempoADormir, tempoObtido, pulos,
		 		 atraso = 0, maxAtrasos = 1, numDeAtrasos = 0, excesso = 0;
	
	public Temporizador(Animacao origem, int tempo)
	{
		this.origem = origem;
		this.TEMPO = tempo * 1000000L; //ms->nano
		
		this.gravadorDeFPS = new double[this.NUM_FPS_PARA_MEDIA];
		this.gravadorDeUPS = new double[this.NUM_FPS_PARA_MEDIA];
		
		this.tempoADormir = this.TEMPO;
		this.tempoDaEstatisticaAnterior = this.tempoAntes = System.nanoTime();
	}
	
	public void normalizar() //chamar esse metodo dentro de um loop
	{
		tempoDepois = System.nanoTime();
		tempoObtido = tempoDepois - tempoAntes;
		
		tempoADormir = (TEMPO - tempoObtido) - atraso;
		
		if(tempoADormir > 0)
		{
			try{Thread.sleep(tempoADormir / 1000000L);}
			catch(Exception e){ e.printStackTrace();}
			
			atraso = (System.nanoTime() - tempoDepois) - tempoADormir;
		}
		else
		{ //tempoADormir <= 0 --> turno demorou tempo demais
			excesso -= tempoADormir; //armazena tempo em excesso
			atraso = 0L;
			if (++numDeAtrasos >= maxAtrasos)
			{
				Thread.yield(); //dar chance a outra thread para executar
				numDeAtrasos = 0;
			}
		}
		tempoAntes = System.nanoTime();
	}
	
	public void pularQuadros()
	{
		pulos = 0;
		while((excesso > this.TEMPO) && (pulos < MAX_PULO_DE_QUADROS))
		{
			excesso -= this.TEMPO;
			
			if(this.origem != null)
				this.origem.atualizar(); //atualiza mas nao renderiza
			
			pulos++;
		}
		quadrosPulados += pulos;
	}
	
	public void gerarEstatisticas()
	{ 
		++contadorDeFrames;
		intervaloParaEstatisticas += TEMPO;
		
	    if(intervaloParaEstatisticas >= MAX_INTERVALO_PARA_ESTATISTICAS)
	    {
	     	double atualFPS = 0.0,
	     		   atualUPS = 0.0,
	     		   totalFPS = 0.0,
	     		   totalUPS = 0.0;
	     		     	
	    	long tempoAgora = System.nanoTime(),
	    		 tempoEntreEstatisticas = tempoAgora - tempoDaEstatisticaAnterior;
	    	
	    	tempoDecorrido += tempoEntreEstatisticas;
	    	
	    	totalDeQuadrosPulados += quadrosPulados;
	    	quadrosPulados = 0;

	    	if(tempoDecorrido > 0)
	    	{
	    		atualFPS = (((double) contadorDeFrames / tempoDecorrido) * 1000000000L);
	    		atualUPS = (((double) (contadorDeFrames + totalDeQuadrosPulados) / tempoDecorrido) * 1000000000L);
	    	}

	    	gravadorDeFPS[(int) contadorDeEstatisticas % NUM_FPS_PARA_MEDIA] = atualFPS;
	    	gravadorDeUPS[(int) contadorDeEstatisticas++ % NUM_FPS_PARA_MEDIA] = atualUPS;

	    	for(int i=0; i < NUM_FPS_PARA_MEDIA; i++)
	    	{
	    		totalFPS += gravadorDeFPS[i];
	    		totalUPS += gravadorDeUPS[i];
	    	}
	    	
	    	if(contadorDeEstatisticas < NUM_FPS_PARA_MEDIA)
	    	{
	    		mediaDeFPS = totalFPS / contadorDeEstatisticas;
	    		mediaDeUPS = totalUPS / contadorDeEstatisticas;
	    	}
	    	else
	    	{
	    		mediaDeFPS = totalFPS / NUM_FPS_PARA_MEDIA;
	    		mediaDeUPS = totalUPS / NUM_FPS_PARA_MEDIA;
	    	}

	    	intervaloParaEstatisticas = 0L;
	    	tempoDaEstatisticaAnterior = tempoAgora;
	    }
	}
	
	public double getFps() { return this.mediaDeFPS; }
	public double getUps() { return this.mediaDeUPS; }
	public long quadrosPulados() { return this.totalDeQuadrosPulados; }
	public long tempoDecorrido() { return this.tempoDecorrido; }
	
	public void reiniciar() {
		intervaloParaEstatisticas = 0L; //em nanosegs
		tempoDaEstatisticaAnterior = 0;  
		contadorDeFrames = 0;
		contadorDeEstatisticas = 0;
		mediaDeFPS = 0.0;
		quadrosPulados = 0L;
		totalDeQuadrosPulados = 0L;
		mediaDeUPS = 0.0;
		tempoDecorrido = 0L;
		pulos = 0;
		atraso = 0;
		numDeAtrasos = 0;
		excesso = 0;
		
		for(int i = 0; i < this.gravadorDeFPS.length; ++i) {
			this.gravadorDeFPS[i] = 0;
			this.gravadorDeUPS[i] = 0;
		}
		
		this.tempoADormir = this.TEMPO;
		this.tempoDaEstatisticaAnterior = this.tempoAntes = System.nanoTime();
	}
}
