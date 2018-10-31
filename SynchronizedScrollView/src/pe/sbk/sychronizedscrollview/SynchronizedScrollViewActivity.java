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
	private ScrollView mScroll1, mScroll2;						//��ũ�� �� 1, 2
	private TextView mText1, mText2;							//�ؽ�Ʈ �� 1, 2
	private RadioGroup mRadio;									//����ȭ ���� ���� �����׷�
	private boolean isSync = true;								//����ȭ ����. �ʱⰪ�� ����ȭ
	private int mTouchStartView = 0;							//���� ����ڰ� ���� �並 ����Ű�� ����
	
	//��ũ�� �� ��ġ �̺�Ʈ ������. ���⼭ ����ȭ�� ���ش�.
	private OnTouchListener mListener = new OnTouchListener() {
		@Override public boolean onTouch(View v, MotionEvent event) {
			//����ȭ�� ���� ������
			if(isSync){
				int id =  v.getId();					//�̺�Ʈ ���� ���� ���̰�
				int action = event.getAction(); 	//�̺�Ʈ ����(�ٿ�, ����, �� ��.)

				//��ġ �ٿ��̺�Ʈ�� ������, ������ ��ġ�� �䰡 ������
				//��, ���� �̺�Ʈ�� ���� �䰡 ����ڰ� ���� ��ġ�� ���̸�
				if(action == MotionEvent.ACTION_DOWN && mTouchStartView == 0)
					mTouchStartView = id;	//���� id�� ����.

				//����ڰ� ��ġ�� �䰡 ��ũ�Ѻ� 1���̰� (2���� �̺�Ʈ�� �����ϱ����� ����)
				//����ڰ� ���� ��ġ�� ���̸� �̺�Ʈ�� �Ѱ��ش�.
				//����ڰ� ���� ��ġ ���� �ʰ� �ٸ� �䰡 �̺�Ʈ�� �Ѱ����� ���� �н�
				if(mTouchStartView == R.id.scroll1 && mTouchStartView == id)
					mScroll2.dispatchTouchEvent(event);

				//2�� ��ũ�� ���̸� 1���� �̺�Ʈ �Ѱ���
				else if(mTouchStartView == R.id.scroll2 && mTouchStartView == id)
					mScroll1.dispatchTouchEvent(event);

				//��ġ�� ������ ���� �� �ʱ�ȭ.
				//�ø��� �� �̺�Ʈ�� ���� �����ϱ� ���ؼ� �������� �˻�.
				//�ø��� �����Ϸ��� ���� �ִ� ��ġ �ٿ� �̺�Ʈ �˻� �ٷ� �������� �ű�� �ø��� �����Ѵ�.
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
		
		mScroll1 = (ScrollView)findViewById(R.id.scroll1);		//��ũ�Ѻ� 1��
		mScroll1.setOnTouchListener(mListener);					//��ġ �̺�Ʈ ������ ���
		
		mScroll2 = (ScrollView)findViewById(R.id.scroll2);		//��ũ�Ѻ� 2��
		mScroll2.setOnTouchListener(mListener);					//��ġ �̺�Ʈ ������ ���
		
		mText1 = (TextView)findViewById(R.id.text1);			//�ؽ�Ʈ �� 1��
		mText2 = (TextView)findViewById(R.id.text2);			//�ؽ�Ʈ �� 2��
		
		mRadio = (RadioGroup)findViewById(R.id.sync);		//����ȭ ���� ���� �׷�
		mRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override public void onCheckedChanged(RadioGroup group, int checkedId) {
				isSync = checkedId == R.id.sync_true ? true:false;
			}
		});

		setTextFromAssetFile(mText1, "text1.txt");		//�ؽ�Ʈ�信 �ؽ�Ʈ ����
		setTextFromAssetFile(mText2, "text2.txt");		//�ؽ�Ʈ�信 �ؽ�Ʈ ����
	}
	
	/**
	 * asset ������ �ִ� ������ �о �ؽ�Ʈ �信 �������ִ� �޼ҵ�
	 * @param tv			- �ؽ�Ʈ ��
	 * @param fileName	- asset�� �ִ� ���� �̸�
	 */
	private void setTextFromAssetFile(TextView tv, String fileName){
		InputStream is = null;					//���� ���� ��ü
		try{
			is = getAssets().open(fileName);	//asset���� ������ ����.
			
			byte[] buffer = new byte[4096];	//���� ����. 4k
			int len = -1;							//���� ũ��
			String text="";						//�ؽ�Ʈ �信 ������ �� �ؽ�Ʈ
			while( (len = is.read(buffer)) >-1 )	//���� �б�
				text += new String(buffer, 0, len, "utf-8");	//�ؽ�Ʈ ����
			
			tv.setText(text);		//�ؽ�Ʈ �信 �ؽ�Ʈ ����
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