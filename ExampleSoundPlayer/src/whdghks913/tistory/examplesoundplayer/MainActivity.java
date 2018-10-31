package whdghks913.tistory.examplesoundplayer;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
	/**
	 * ���� ��ü�� ������ �ִ� �κ��Դϴ�
	 * �� �������� ����� ���� ��ư�� ��ũ��, �����÷��̾� �Դϴ�
	 */
	Button button;
	SeekBar seekbar;
	MediaPlayer music;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/**
		 * MusicPlayer�� �����ϴ� �κ��Դϴ�
		 * R.raw.konan�� ���带 ����� ��ġ�̸�, �Ʒ� �ּ�ó�� ������ �ҷ��ü��� �ִ�
		 */
		/**
		 * String path = Environment.getDataDirectory().getAbsolutePath();
		 * ����̽��� �����η� ����Ʈ �Ǵ� ��ġ�� ���ɴϴ�

		 * String mp3file_name = "/sound.mp3";
		 * ����� ������ ��θ� �����մϴ�

		 * music.setDataSource(path + mp3file_name);
		 * setDataSource�޼ҵ带 �̿��Ͽ� ���� �� ��θ� ��Ĩ�ϴ�
		 */
		music = MediaPlayer.create(this, R.raw.konan);
		
		/**
		 * ���� �ݺ��� �����ϴ� �κ�
		 * true : ���ѹݺ� ����, false : ���ѹݺ� ����
		 */
		music.setLooping(true);
		
		button = (Button) findViewById(R.id.button1);
		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		
		/**
		 * seekbar�� �ִ��� ������ �ִ����, �� music.getDuration()�� ���� ���� �����մϴ�
		 */
		seekbar.setMax(music.getDuration());
		
		/**
		 * ��ũ�ٸ� ���������� ���� ��� ��ġ�� ���Ҽ� �ֵ��� �����մϴ�
		 */
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				/**
				 * ����°�� �Ѿ���� boolean fromUser�� ��� true�϶��� ����ڰ� ���� �����ΰ��,
				 * false�ΰ�쿡�� �ҽ���, ���û󿡼� �����ΰ���̸�
				 * ���⼭�� ����ڰ� ���� ������ ��쿡�� �۵��ϵ��� if���� �������
				 * 
				 * ���� : if���� { } ��ȣ ���� ���� �����ϰ�� ������ �����մϴ�
				 */
				if(fromUser)
					music.seekTo(progress);
			}
		});
	}

	public void button(View v){
		/**
		 * music.isPlaying()�� true : ������ ���� ������Դϴ�, false : ������� �ƴմϴ�
		 */
		if(music.isPlaying()){
			// ������ �����մϴ�
			music.stop();
			try {
				// ������ ����Ұ�츦 ����� �غ��մϴ�
				// prepare()�� ���ܰ� 2������ �ʿ��մϴ�
				music.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ���� ���� ������ 0, �� ó������ �ǵ����ϴ�
			music.seekTo(0);
			
			// ��ư�� ���ڸ� ��������, ��ũ�ٸ� ó������ �ǵ����ϴ�
			button.setText(R.string.start);
			seekbar.setProgress(0);
		}else{
			// ������ �����մϴ�
			music.start();
			button.setText(R.string.stop);
			
			/**
			 * �����带 ���� 1�ʸ��� SeekBar�� �����̰� �մϴ�
			 */
			Thread();
		}
	}
	
	/**
	 * ������� �ѹ��� ����Ҽ� �����Ƿ� ���� �޼ҵ�ȭ �Ͽ� ����ø��� �ٽ� �����մϴ�
	 * 
	 * ���� : http://indy9052.blog.me/120142002766
	 * http://naiacinn.tistory.com/109
	 * http://nephilim.tistory.com/56
	 * (�����尡 �̹� ������� ���¿��� start()�޼ҵ尡 2���̻� ȣ��Ǹ� �������� ������ �߻��ϸ�
	 * ������� �ѹ��� �����Ҽ� �ֽ��ϴ�, �� �ѹ� ������ ������ �մϴ�)
	 */
	public void Thread(){
		Runnable task = new Runnable(){
			public void run(){
				/**
				 * while���� ������ ������ �������϶� �Լ� ���ư��� �մϴ�
				 */
				while(music.isPlaying()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/**
					 * music.getCurrentPosition()�� ���� ���� ��� ��ġ�� �������� ���� �Դϴ�
					 */
					seekbar.setProgress(music.getCurrentPosition());
				}
			}
		};
		Thread thread = new Thread(task);
		thread.start();
	}

}
