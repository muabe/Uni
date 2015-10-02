
안드로이드를 개발하다 보면 굉장히 어려운 UI Manipulation을 겪게 됩니다.<br>
특히 Thread사용후 UI 화면전환은 개발에 큰 걸림돌이 되곤 합니다.<br>
개발자에게 복잡한 GUI는 비지니스 로직에 투자할 시간을 GUI 코딩에 허비하게 만듭니다.<br>
우리는 좀더 개발 생산성을 위해 새로운 패턴이 필요합니다.


<br>
### Gradle
```
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.JaeWoongOh:Uni:+'
}
```


<br>
Uni Framework
========
Activity와 Flagement의 onCreate() 메소드 안에 복잡하고 지저분한 코드들을 볼수 있습니다.<br>
또한 Layout이 어디서 구현되어 있는지 소스코드를 이리저리 찾는것을 경험하게 됩니다.<br>

Uni는 Layout과 Class를 하나로 묶어주고 각각의 화면에 소스코드를 정의하는 패턴을 사용합니다.<br>
Uni는 여러개로 분리된 Layout들이 독립적으로 자기의 할일을 수행하는 방식을 말합니다.<br>
다시말해 Layout에 해당하는 Class를 매핑하고 화면별로 업무를 정의합니다. <br>
> Uni에서는 Layout과 매핑되는 Class를 Viewer라고 합니다.

```java
@Layout(R.layout.main)
public class MainViewer extends Viewer{
	public void onPost(int requestCode){
		// 해당 화면업무 수행
	}
}
```

- 화면 재활용
 화면을 재활용하기 위해 Layout을 분리하게 됩니다. 하지만 Layout을 구현하는 code는 Activity, Flagement에 의존적이여서 class를 구성하는데 어려움을 겪습니다.
> 화면별 class 분리를 위해 Activity, Flagement를 반드시 참조해야한다.

Uni는 layout에 해당하는 업무를 완변히 분리해줍니다.<br>
기존에 분리가 어려웠던 이유는 Activity, Flagement의 리소스를 사용할수 없기 때문입니다.<br>
Uni에서는 기존에 동일한 패턴으로 리소스를 사용할수 있게 해줍니다.
```java
@Layout(R.layout.main)
public class MainViewer extends Viewer{
	public void onPost(int requestCode){
		TextView text = (TextView)findById(R.id.text);
	}
}
```





Viewer는 Async한 화면을 구성하기 위한 Thread가 내장되어 있으며 


Uni 에는 다음과 같은 기능들이 있습니다.
- Quick and easy UI Manipulation
- Less Code
- Convenient Async Callback
- Binding
- Support multiple versions

##Quick and easy UI Manipulation
현실적으로 GUI 개발에 있어서 안드로이드는 매우 취약합니다. <br>
전체적인 layout 구조와 설계와 이벤트에 따른 동적 화면을 표현 하기가 쉽지 않습니다.<br> 
XML Layout을 구현하는 code는 Activity, Flagement 등에 의존적이여서 code의 분류와 구성또한 어렵습니다.<br>
Uni는 Activity, Flagement에 영향을 받지 않아 code를 화면별 조각인 Viewer로 분류하고 
Viewer를 조합하여 새로운 화면을 구성합니다.
하나의 Layout에 필요한 Viewer를 조합하여 동적으로 바인딩하는것을 기본으로 합니다.<br>
이런점은 이벤트나 특정 상황에서 쉽게 동적인 표현을 할수 있게 해줍니다.<br>
다시말해 컴퍼넌트 형식 처리로 Layout에 전체가 아닌 특정 부분만 바인딩하여
시스템 자원과 개발 시간을 단축할 수 있습니다.<br>
여기에 Viewer에 내장된 Injection, Async init, method binding 기능은 쉽고 빠르게
UI Manipulation을 하도록 도와 줍니다.

##Convenient Async UI Manipulation<br>
Viewer는 바인딩 되기전 AsyncTask를 수행할수 있는 기능을 지원합니다.<br>
Viewer 내부의 loading 메소드를 재정의 함으로써 쉽게 AsyncTask를 사용할수 있으며
AsyncTask가 수행하는 동안 Viewer load 화면을 설정 할수 있습니다.

[채팅 화면을 Async로 갱신하는 예제]
```java
.... 
//Async로 화면 바인딩
getViewer(R.layout.sub,Test.class).change(ParentsView);
....

//바인딩을 받는 Viewer
public class Test extends Viewer{
	@Override
	public boolean onLoading() {
		/* 네트워크 및 Thread 작업 실행 */
		setLoadingParameter("say", "What's up?");//결과 설정
		return true;
	}
	@Override
	public void onPost() {
		TextView(R.id.say_text).setText((String)getLoadingParameter("say"));
	}
}
```

##Less Code
View Injection 을 활용할수 있는 Annotion과 UI Controller를 이용하면
코드가 엄청나게 줄어들 수 있는 다양한 기능이 있습니다.<br>
Viewer방식으로 화면별/기능별로 Viewer를 분류하므로써 기존코드를 활용하고
간결하며 직관적인 코드로 관리할수 있습니다.<br>
또한 A-Query, Roboguice나 Android annotation같은 외부 라이브러리를
아무런 제약없이 조합함으로써 더좋은 개발 퍼포먼스 낼수 있습니다.

ex) 아래는 같은 동일한 역할을 하는 소스코드 비교 예제입니다.<br>
 [기존]
```java
	public class MainActivity extends Activity{
		private TextView text;
		private Button btn1;
		private Button btn2;
		private Button btn3;
		private Button subActivity;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.sample_projects);
			
			text=(TextView)findViewById(R.id.text);
			String param = getIntent().getExtras().getString("name");
			text.setText(param); //외부에서 받아온 parameter 출력
			
			btn1 = (Button)findViewById(R.id.btn1); //버튼1 Click
			btn1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("SubActivity","btn1 Click!");
				}
			});
			
			btn2 = (Button)findViewById(R.id.btn2); //버튼2 Click
			btn2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("SubActivity","btn2 Click!");
				}
			});
			
			btn3 = (Button)findViewById(R.id.btn3); //버튼3 Click
			btn3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("SubActivity","btn3 Click!");
				}
			});
			
			subActivity = (Button)findViewById(R.id.subActivity); //SubActivity 호출
			subActivity.setOnClickListener(new SubActivityShow("What's up?"));
		}
		
		class SubActivityShow impelements OnClickListener{
			private String say;
			
			public SubActivityShow(String say){
				this.say = say;
			}
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SubActivity.class);
				intent.putExtra("say", say);
				startActivity(intent);
			}
		}
	}
```
	
  [Uni]
 ```java
	public class MainViewer extends Viewer{
		@getViewClick Button btn1; // Annotion injection
		@getViewClick Button btn2;
		@getViewClick Button btn3
		
		@Override
		public void view_init() {
			TextView(R.id.text).setText(getParam("name")); //외부에서 받아온 parameter 출력 
			setOnClickParamListener("sub_Viewer", R.id.sub_Viewer, "What's up?"); // Code base injection
		}
		
		public void sub_Viewer(View v, String clickParam){ //SubViewer 호출
			getViewer(R.layout.sub,SubViewer.class).addParam("say", clickParam).change(getParent());
		}
		public void btn1(View v) { //버튼1 Click
			Log.d("SubActivity","btn1 Click!");
		}
		public void btn2(View v) { //버튼2 Click
			Log.d("SubActivity","btn2 Click!");
		}
		public void btn3(View v) { //버튼3 Click
			Log.d("SubActivity","btn3 Click!");
		}
	}
```

##Binding
Uni를 사용하면 Listener를 사용하기 편리해 집니다. <br>
Listener 클래스를 만들지 않아도 되고 내부 클래스로 만들지 않아도 되고<br>
Listener 인터페이스를 implement 하지 않아도 됩니다.<br> 
Listener를 바인딩 할때 Parameter 또한 쉽게 전달 할수 있습니다.<br>
Annotation과 code로 Listener를 바인딩 할수 있는 두가지 방법을 제공합니다.

[Code Base 방식]
```java
//함수명을 "say"를 명시하고 파라미터를 던져줄수 있다.
public void view_init(){
	String param = "what's up?"
	setOnClickParamListener("say", R.id.say, param); 
}
//파라미터를 전달 받을수 있다.
public void say(View v, Object param){
		Log.d("Uni",param.toString());
}
```
[Annotation방식]
```java
해당 아이디명과 listener함수명 동일
@getViewClick View say; //객체 Injection

public void say(View v){
		Log.d("Uni","What's up?");
}
```







