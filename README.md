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
#Uni Framework

Activity와 Flagement의 onCreate() 메소드 안에 복잡하고 지저분한 코드들을 볼수 있습니다.<br>
또한 Layout이 어디서 구현되어 있는지 소스코드를 이리저리 찾는것을 경험하게 됩니다.<br>

Uni는 Layout과 Class를 하나로 묶어주고 각각의 화면에 소스코드를 정의하는 패턴을 사용합니다.<br>
Uni는 여러개로 분리된 Layout들이 독립적으로 자기의 할일을 수행하는 방식을 말합니다.<br>
다시말해 Layout에 해당하는 Class를 매핑하고 화면별로 업무를 정의합니다. <br>

Uni에서는 Layout과 매핑되는 Class를 Viewer라고 합니다.<br>
Viewer방식으로 화면별/기능별로 Viewer를 분류하므로써 기존코드를 활용하고
간결하며 직관적인 코드로 관리할수 있습니다.<br>

```java
@Layout(R.layout.main)
public class MainViewer extends Viewer{
	public void onPost(int requestCode){
		// 해당 화면업무 수행
	}
}
```

Uni 에는 다음과 같은 내용으로 개발 생산성을 향상시켜줍니다.
 - Code Less를 위한 Annotation 지원
 - 동적 화면 구성
 - 화면 재활용
 - 비동기 지원
 - 업무별 기능 분리
 

###Code Less를 위한 Annotation 지원
Viewer는 Injection, method binding을 Annotation으로 지원하여  쉽고 빠르게
UI Manipulation을 하도록 도와 줍니다.

- before

	```java
	public class Main extends Activity{
		private TextView text;
		private Button btn1;
		private Button btn2;
		private Button btn3;
		private Button subActivity;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity);
			text = (TextView)findById(R.id.text);
			btn1 = (Button)findById(R.id.btn1);
			btn2 = (Button)findById(R.id.btn2);
			btn3 = (Button)findById(R.id.btn3);
			subActivity = (Button)findById(R.id.subActivity);
		
			text.setText("hello");
			btn1.setOnClickListener(new ResetText("Uni"));
		}
		
		class ResetText impelements OnClickListener{
			String msg;
			public ResetText(String msg){
				this.msg = msg;
			}
			@Override
		        public void onClick(View v) {
		            text.setText(msg);
		        }
		}
	}

	```

- Uni

	```java
	@Layout(R.layout.main)
	public class MainViewer extends Viewer {
		@GetView TextView text;
		@GetView Button btn1, btn2, btn3, subActivity;
	
		public void onPost(int requestCode){
			text.setText("hello");
			setOnClickParam("Uni");
		}
		
		@OnClick
		public void btn3(View view, String msg){
			text.setText(msg);
		}
	}

	```

<br>
###동적 화면구성
 Fragment와 같은 방식으로 UI를 Replace하는 방식과 동일합니다.<br>
하지만 Uni는 여러개의 화면을 구성하는데 있어서 매우 빠르고 쉽습니다.<br>
Replace가 수행할때 비동기 방식으로 화면을 불러들일수 있으며 화면을 쉽게 구현하기 위한 많은 내부 함수들이 있습니다.<br>
Fragment를 큰 뼈대를 잡고 하위 UI를 Viewer로 구성하는데 매우 적합 합니다.
```java
public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		Viewer.build(MainViewer.class, this)
			.addParam("param1","hello").addParam("param2","world") //파라미터를 쉽게 전달할수 있음
			.change(R.id.frame);
	}
}
```

<br>
###화면 재활용

 화면을 재활용하기 위해 Layout을 분리하게 됩니다. 하지만 Layout을 구현하는 code는 Activity, Flagement에 의존적이여서 class를 구성하는데 어려움을 겪습니다.
> 화면별 class 분리를 위해 Activity, Flagement를 반드시 참조해야한다.

 Uni는 layout에 해당하는 업무를 완벽히 분리해줍니다.<br>
기존에 분리가 어려웠던 이유는 Activity, Flagement의 리소스를 사용할수 없기 때문입니다.<br>
Uni에서는 기존에 동일한 패턴으로 리소스를 사용할수 있게 해줍니다.
 ```java
@Layout(R.layout.main)
public class MainViewer extends Viewer{
	public void onPost(int requestCode){
		TextView text = (TextView)findViewById(R.id.text);
	}
}
 ```

<br>
###비동기 지원

 Viewer는 네트워크 등 비동기 화면을 구성하기 위한 Thread가 내장되어 있습니다.<br>
AsyncTask와 같은 패턴으로 구현되어 있어 개발 접근성이 매우 높습니다.<br>

Viewer가 화면에 add될때 Background 작업을 할수 있는 onLoad()를 함수를 제공되어 별도의 Thread를 구현할 필요가 없습니다.
 ```java
@Layout(R.layout.main)
public class MainViewer extends Viewer{
    public boolean onLoad(int requestCode, UpdateEvent event) {
	//통신후 결과 저장
	addParam("result", resultClass);
	return true;
    }
    public void onPost(int requestCode) {
    	//결과에 대한 UI 구성
    	resultClass = getParam("result");
    }
}
 ```
 
<br> 
###업무별 기능 분리
화면 refresh하거나 기능별 화면을 구성할때 requestCode로 구분하여 기능을 분리 할수 있습니다.<br>
이처럼 업무별 기능을 분리함으로써 코드 재활용 및 간결한 코드패턴이 가능합니다.
```java
 public boolean onLoad(int requestCode, UpdateEvent event) {
	 switch (requestCode){
    		case 1:
    		//A 업무 통신
    		case 2:
    		//B 업무 통신
	}
    	//결과 저장
	addParam("result", resultClass);
	return true;
}
	
public void onPost(int requestCode) {
    	resultClass = getParam("result");
    	switch (requestCode){
    		case 1:
    		//A 화면 기능정의
    		case 2:
    		//B 화면 기능정의
	}
}
```

<br>






