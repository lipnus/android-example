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
	 * 각종 개체를 선언해 주는 부분입니다
	 * 이 예제에서 선언된 것은 버튼과 시크바, 뮤직플레이어 입니다
	 */
	Button button;
	SeekBar seekbar;
	MediaPlayer music;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/**
		 * MusicPlayer를 정의하는 부분입니다
		 * R.raw.konan은 사운드를 재생할 위치이며, 아래 주석처럼 파일을 불러올수도 있다
		 */
		/**
		 * String path = Environment.getDataDirectory().getAbsolutePath();
		 * 디바이스의 절대경로로 마운트 되는 위치를 얻어옵니다

		 * String mp3file_name = "/sound.mp3";
		 * 재생할 파일의 경로를 지정합니다

		 * music.setDataSource(path + mp3file_name);
		 * setDataSource메소드를 이용하여 얻은 두 경로를 합칩니다
		 */
		music = MediaPlayer.create(this, R.raw.konan);
		
		/**
		 * 무한 반복을 설정하는 부분
		 * true : 무한반복 설정, false : 무한반복 해제
		 */
		music.setLooping(true);
		
		button = (Button) findViewById(R.id.button1);
		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		
		/**
		 * seekbar의 최댓값을 음악의 최대길이, 즉 music.getDuration()의 값을 얻어와 지정합니다
		 */
		seekbar.setMax(music.getDuration());
		
		/**
		 * 시크바를 움직였을떄 음악 재생 위치도 변할수 있도록 지정합니다
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
				 * 세번째로 넘어오는 boolean fromUser의 경우 true일때는 사용자가 직접 움직인경우,
				 * false인경우에는 소스상, 어플상에서 움직인경우이며
				 * 여기서는 사용자가 직접 움직인 경우에만 작동하도록 if문을 만들었다
				 * 
				 * 참고 : if문등 { } 괄호 안의 줄이 한줄일경우 생략이 가능합니다
				 */
				if(fromUser)
					music.seekTo(progress);
			}
		});
	}

	public void button(View v){
		/**
		 * music.isPlaying()이 true : 음악이 현재 재생중입니다, false : 재생중이 아닙니다
		 */
		if(music.isPlaying()){
			// 음악을 정지합니다
			music.stop();
			try {
				// 음악을 재생할경우를 대비해 준비합니다
				// prepare()은 예외가 2가지나 필요합니다
				music.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 음악 진행 정도를 0, 즉 처음으로 되돌립니다
			music.seekTo(0);
			
			// 버튼의 글자를 시작으로, 시크바를 처음으로 되돌립니다
			button.setText(R.string.start);
			seekbar.setProgress(0);
		}else{
			// 음악을 실행합니다
			music.start();
			button.setText(R.string.stop);
			
			/**
			 * 쓰래드를 돌려 1초마다 SeekBar를 움직이게 합니다
			 */
			Thread();
		}
	}
	
	/**
	 * 쓰래드는 한번만 사용할수 있으므로 따로 메소드화 하여 실행시마다 다시 재사용합니다
	 * 
	 * 참조 : http://indy9052.blog.me/120142002766
	 * http://naiacinn.tistory.com/109
	 * http://nephilim.tistory.com/56
	 * (쓰래드가 이미 만들어진 상태에서 start()메소드가 2번이상 호출되면 강제종료 오류가 발생하며
	 * 쓰래드는 한번만 실행할수 있습니다, 즉 한번 실행후 버려야 합니다)
	 */
	public void Thread(){
		Runnable task = new Runnable(){
			public void run(){
				/**
				 * while문을 돌려서 음악이 실행중일때 게속 돌아가게 합니다
				 */
				while(music.isPlaying()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/**
					 * music.getCurrentPosition()은 현재 음악 재생 위치를 가져오는 구문 입니다
					 */
					seekbar.setProgress(music.getCurrentPosition());
				}
			}
		};
		Thread thread = new Thread(task);
		thread.start();
	}

}
