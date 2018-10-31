package pe.sbk.sychronizedscrollview;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class SynchronizedScrollViewActivity extends Activity {
	private ScrollView mScroll1, mScroll2;						//스크롤 뷰 1, 2
	private TextView mText1, mText2;							//텍스트 뷰 1, 2
	private RadioGroup mRadio;									//동기화 여부 선택 라디오그룹
	private boolean isSync = true;								//동기화 여부. 초기값은 동기화
	private int mTouchStartView = 0;							//실제 사용자가 터한 뷰를 가르키는 변수
	
	//스크롤 뷰 터치 이벤트 리스너. 여기서 동기화를 해준다.
	private OnTouchListener mListener = new OnTouchListener() {
		@Override public boolean onTouch(View v, MotionEvent event) {
			//동기화를 선택 했으면
			if(isSync){
				int id =  v.getId();					//이벤트 들어온 뷰의 아이값
				int action = event.getAction(); 	//이벤트 동작(다운, 무브, 업 등.)

				//터치 다운이벤트가 들어오고, 기존에 터치된 뷰가 없으면
				//즉, 현재 이벤트가 들어온 뷰가 사용자가 직접 터치한 뷰이면
				if(action == MotionEvent.ACTION_DOWN && mTouchStartView == 0)
					mTouchStartView = id;	//뷰의 id값 저장.

				//사용자가 터치한 뷰가 스크롤뷰 1번이고 (2번에 이벤트를 전달하기위해 구분)
				//사용자가 직접 터치한 뷰이면 이벤트를 넘겨준다.
				//사용자가 직접 터치 하지 않고 다른 뷰가 이벤트를 넘겨줬을 경우는 패스
				if(mTouchStartView == R.id.scroll1 && mTouchStartView == id)
					mScroll2.dispatchTouchEvent(event);

				//2번 스크롤 뷰이면 1번에 이벤트 넘겨줌
				else if(mTouchStartView == R.id.scroll2 && mTouchStartView == id)
					mScroll1.dispatchTouchEvent(event);

				//터치가 끝나면 변수 값 초기화.
				//플링시 그 이벤트도 같이 전달하기 위해서 마지막에 검사.
				//플링은 무시하려면 위에 있는 터치 다운 이벤트 검사 바로 다음으로 옮기면 플링은 무시한다.
				if(action ==MotionEvent.ACTION_UP)
					mTouchStartView = 0;
			}

			return false;
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mScroll1 = (ScrollView)findViewById(R.id.scroll1);		//스크롤뷰 1번
		mScroll1.setOnTouchListener(mListener);					//터치 이벤트 리스너 등록
		
		mScroll2 = (ScrollView)findViewById(R.id.scroll2);		//스크롤뷰 2번
		mScroll2.setOnTouchListener(mListener);					//터치 이벤트 리스너 등록
		
		mText1 = (TextView)findViewById(R.id.text1);			//텍스트 뷰 1번
		mText2 = (TextView)findViewById(R.id.text2);			//텍스트 뷰 2번
		
		mRadio = (RadioGroup)findViewById(R.id.sync);		//동기화 선택 라디오 그룹
		mRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override public void onCheckedChanged(RadioGroup group, int checkedId) {
				isSync = checkedId == R.id.sync_true ? true:false;
			}
		});

		setTextFromAssetFile(mText1, "text1.txt");		//텍스트뷰에 텍스트 설정
		setTextFromAssetFile(mText2, "text2.txt");		//텍스트뷰에 텍스트 설정
	}
	
	/**
	 * asset 폴더에 있는 파일을 읽어서 텍스트 뷰에 설정해주는 메소드
	 * @param tv			- 텍스트 뷰
	 * @param fileName	- asset에 있는 파일 이름
	 */
	private void setTextFromAssetFile(TextView tv, String fileName){
		InputStream is = null;					//파일 읽을 객체
		try{
			is = getAssets().open(fileName);	//asset에서 파일을 열음.
			
			byte[] buffer = new byte[4096];	//버퍼 생성. 4k
			int len = -1;							//읽은 크기
			String text="";						//텍스트 뷰에 설정해 줄 텍스트
			while( (len = is.read(buffer)) >-1 )	//파일 읽기
				text += new String(buffer, 0, len, "utf-8");	//텍스트 생성
			
			tv.setText(text);		//텍스트 뷰에 텍스트 설정
		}
		catch(Exception e){e.printStackTrace();}
		finally{
			try{
				if(is != null) is.close();
				is = null;
			}catch(Exception e){e.printStackTrace();}
		}
	}
}