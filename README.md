MarkJ Android Framework
========
안드로이드를 개발하다 보면 굉장히 어려운 UI Manipulation을 겪게 됩니다.<br>
특히 AsyncTask와 UI 화면전환은 개발에 큰 걸림돌이 되곤 합니다.<br>
개발자에게 복잡한 GUI는 비지니스 로직에 투자할 시간을 GUI 코딩에 허비하게 만듭니다.<br>
UI Manipulation을 쉽고 빠르게 개발할수 있는 라이브러리가 필요했고
이런점을 해결하기 위해 나온 라이브러리가 MarkJ입니다.<br>

MarkJ 에는 다음과 같은 기능들이 있습니다.
- Less Code
- Quick and easy UI Manipulation
- Convenient Async Callback
- Binding
- Bluetooth
- Custom view 
- Utillity
- Support multiple versions

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
	
  [MarkJ]
 ```java
	public class MainViewer extends JwViewer{
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

##Quick and easy UI Manipulation
현실적으로 GUI 개발에 있어서 안드로이드는 매우 취약합니다. <br>
원하는 디자인과 구성을 위해때론 많을 시간을 소비해야할 때가 많습니다.<br>
기본 View에 복잡한 디자인 적용이 어렵고 이벤트에 따른 동적 화면을 표현 하기가 쉽지 않습니다.<br> 
XML Layout을 구현하는 code는 Activity, Flagement 등에 의존적이여서 code의 분류와 구성또한 어렵습니다.<br>
MarkJ는 Activity, Flagement에 영향을 받지 않아 code를 화면별 조각인 Viewer로 분류하고 
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
public class Test extends JwViewer{
	@Override
	public boolean loading() {
		/* 네트워크 및 Thread 작업 실행 */
		setLoadingParameter("say", "What's up?");//결과 설정
		return true;
	}
	@Override
	public void view_init() {
		TextView(R.id.say_text).setText((String)getLoadingParameter("say"));
	}
}
```



##Binding
MarkJ를 사용하면 Listener를 사용하기 편리해 집니다. <br>
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
		Log.d("MarkJ",param.toString());
}
```
[Annotation방식]
```java
해당 아이디명과 listener함수명 동일
@getViewClick View say; //객체 Injection

public void say(View v){
		Log.d("MarkJ","What's up?");
}
```
##Custom view 
 GUI 개발시 화려한 디자인 구현과 단말 기종에 따른 스크린사이즈 문제에 어려움이 있습니다.<br>
 예를들어 WheelView 나 그래프의 경우 오픈소스를 많이 쓰고 있습니다.<br>
 오픈소스는 필요한 디자인을 적용하는데 한계가 있고 Code로 사이즈를 지정하는 등 여러 단말에 적용하기가 까다롭습니다.<br>
 Code Base형태로 제공되는 오픈소스 Custom View는 구현과 디자인 적용에 어려운 실정 입니다.<br>
 MarkJ에서 제공하는 Custom View는 IDE Layout Editor에서 Drag and Drop 형태로 제공하고 있어 굉장히 쉽고 편리합니다.<br>
 또한 그위에 디자인을 입히는 형식이라 복잡한 디자인을 쉽게 적용할수 있습니다.<br>
 Touch 및 Animation 등의 모듈을 제공하여 Active한 화면을 구성할수 있게 도와 줍니다.<br>
 MarkJ를 사용하면 android에서 제공하는 Widgets의 대부분을 custom하여 제공하고 있으며 <br>
 그 밖에 멀티 윈도우, 각종 그래프 등등 개발에 유용한 주요 Custom View를 다수 제공하고 있습니다.<br>

```
Drag and Drop으로 쉽게 Custom View 사용
```  
![](http://coosadb.cafe24.com/img/drag.jpg)   
```
다양한 Custom View가 있지만 몇가지만 소개 합니다.
-각종 Graph View, Wheel View, Paper View
```
![](http://coosadb.cafe24.com/img/custom1.jpg) &nbsp; ![](http://coosadb.cafe24.com/img/custom2.jpg) &nbsp; ![](http://coosadb.cafe24.com/img/custom4.jpg)   
 
![](http://coosadb.cafe24.com/img/custom3.jpg) ![](http://coosadb.cafe24.com/img/custom5.jpg)

##Utillity
개발에 도움을 주는 Utillity를 쉽게 사용할수 있는 모듈을 제공합니다.
 - XML,JSon Parser/converter <br>
 - File handler  <br>
 - Bluetooth(Multiple access 지원) <br>
 - Sound  <br>
 - WebConnection <br>
 - GUI Theme <br>
 - Preperence DataBase <br>
 - 기타 UI Manipulation을 위한 전반적인 모듈제공
 
```
 MarkJ Utillity 사용 스크린샷(혈당관리 Sample App)
 1.다중접속이 가능한 bluetooth 라이브러리사용
 2.MarkJ GUI Theme 적용 
``` 
![](http://coosadb.cafe24.com/img/glucose.jpg) 



##Support multiple versions
하나의 코드로 여러개의 Android Version을 지원할수 있습니다.<br>
Viewer는 view의 기본 속성만으 사용하여 <br>
Android 2.2이상 모든 버전에 호환이 가능합니다.<br>
*Custom View의 경우 4.0이상 추가된 기능에 따라 만들어진 View는 MarkJ.v14 라이브러리로 따로 제공







