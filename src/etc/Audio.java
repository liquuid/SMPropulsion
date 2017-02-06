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
import java.io.File;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Audio
{
	//efeitos sonoros (wav, mid)//////////////////////////////////////
	private AudioInputStream soundStream;
	private Clip som;
	
	public void tocar(File arquivo)
	{
		try{carregar(arquivo).start();}
		catch(Exception e){System.out.println("Problemas no audio - tocar(File)");};
	}
	
	public boolean estaReproduzindo() {
		return this.som.isRunning();
	}
	
	private Clip carregar(File aquivo) throws Exception
	{
		soundStream = AudioSystem.getAudioInputStream(aquivo);
		
		AudioFormat audioFormat = soundStream.getFormat();
			
		DataLine.Info dataLineInfo = new DataLine.Info(
				Clip.class,
				AudioSystem.getTargetFormats(AudioFormat.Encoding.PCM_SIGNED, audioFormat ),
				audioFormat.getFrameSize(),
				audioFormat.getFrameSize() * 2
		);
		
		if (!AudioSystem.isLineSupported(dataLineInfo))
			 System.err.println("Unsupported Clip File!");
			
		som = (Clip) AudioSystem.getLine(dataLineInfo);
		som.open(soundStream);
		
		return som;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	//musicas (mid)
	public Sequencer sequenciador;
	
	public Audio()
	{	
		try
		{
			sequenciador = MidiSystem.getSequencer();
			sequenciador.open();
		}
		catch (Exception e)
		{
			System.out.println("Problemas no audio - Audio()");
		}
	}
	
	public boolean estaTocando()
	{
		return this.sequenciador.isRunning();
	}
	
	public void pararMusica()
	{
		this.sequenciador.stop();
	}
	
	public void tocarMusica(File musica)
	{
		try
		{
			sequenciador.stop();
			sequenciador.setSequence(MidiSystem.getSequence(musica));
			sequenciador.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequenciador.start();
		}
		catch (Exception e)
		{
			System.out.println("Problemas no audio - tocarMusica(File)");
		}
	}
	/////////////////////////////////////////////
}