package com.markjmind.fragmenttest;

import android.view.View;
import android.widget.Button;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.UniTaskAdapter;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.Progress;
import com.markjmind.uni.progress.UniProgress;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */

@Layout(R.layout.item)
@Progress(mode = UniProgress.VIEW, res=R.layout.one_progress)
public class Menu3Fragment extends UniFragment {
    @GetView
    Button btn;


    @Override
    public void onBind() {

    }

    @Override
    public void onPre() {
        progress.param.add("textName", "하이");
    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        for(int i=0;i<=100;i++){
            event.update(i);
            Thread.sleep(10);
        }
        param.add("hi","hi");

    }

    @Override
    public void onPost() {
        btn.setText(param.getString("hi"));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.param.add("textName", "thread");
                progress.set(UniProgress.VIEW, new SimpleProgressBar());
//                progress.get().setMode(U```niProgress.DIALOG);
                excute(new UniTaskAdapter(Menu3Fragment.this) {

                    @Override
                    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
                        for (int i = 0; i <= 100; i++) {
                            event.update(i);
                            Thread.sleep(10);
                        }
                    }

                    @Override
                    public void onPost() {
                        btn.setText("완료");
                    }

                });
//                SimpleDialog d = new SimpleDialog(getActivity());
//                d.show();
            }
        });
    }

    public String getMode(int mode){
        if(mode==0){
            return "VIEW";
        }else{
            return "DIALGO";
        }
    }

}














































/**
  1. 프로필 수정에 관련된 디자인 및 가이드가 전혀 없다.
  2. 수정할수 없는 항목(아이디 등)에 대한 내용이 없다.
  3. 수정화면에서 나타나지 않는 항목(비밀번호 입력 등)에 대한 내용이 없다.
  4. 전체 항목들에 대한 조건/규칙 및 설명이 전혀 없음
     - 아이디 규칙 ex)아이디는 1~10 자리 영문으로함
     - 비밀번호 규칙 ex)5자리 이상 영문,숫자,특수문자 조합
     - 생년월일 음력,양력 처리 및 표시
     - 현재 거주지 입력은 시,군,구를 순차적으로 입력한다던지
       또는 동/건물명을 검색하여 주소를 입력하는 등에 대한 방법을 제시하지 않음






 * 프로필 수정

 ● 기획서의 정책 설명/내용 부족

 회입가입과 회원정보 수정은 항목은 비슷해 보이나 많이 다르다.
 예를 들어 최초 회원가입시 아이디는 회원을 구분을 고유 아이디기 때문에 변경할수 없다.
 이렇기 때문에 수정에는 면밀히 항목들을 분석해서 기획서에 기술해주어야 한다.

 하지만 P53에서 회원수정은 "회원 가입시 입력했던 정보 확인 및 수정 가능함"이라 적혀 있다.
 다시말해 회원수정은 회원가입과 동일하다고 명시한것이다.

 이로인해 의문점과 개발을 할수 없는 문제점이 발생한다.
 아이디,성별,이름,생일등 모든정보를 변경할수 있어 완전히 다른 사람으로 재입가입 할수 있다.
 해당앱은 유료 결제앱임에도 불구하고 다른사람에게 양도가 가능할 정도이다.
 일반적으로 타 앱에서는 아이디,이름,성별,생일은 변경할수 없게 되어있다.
 웨딩TV앱은 타 앱에서 요구하는 회원정보 보다 몇배나 많은 항목들이 있고
 이에 따라 변경할수 없는 더많은 항목들이 많이 존재할것이다.

 예를 들어 아래는 아주 기본적인 의문점들이다.
 1.재혼서류를 등록했는데 다시 미혼으로 돌아 갈수 있는가?
 2.인종이 바뀔수가 있는가?
 3.본인확인을 한후 이름,성별등을 변경하여 다른사람 행세를 할수 있는가?
 4.장애등급이 있다가 없어질수 있는가?
 5.국적, 시민권이 수시로 바뀔수가 있는가?
 6.출산을 했는데 다시 미출산이 될수 있는가?
 7.아이디, 성별, 이름, 생일이 변경될수 있는가?
 8.돌아가신 부모님이 다시 살아돌아올수 있는가?

 위 사항에 대해서 일반적으로 타사에서는 앱상에서 직접수정을 하지못하고
 고객센터에 확인 절차를 거쳐야하거나 회원탈퇴후 재가입해야 되는 경우이다.

 P54에서 회원정보 수정에서 기본항목만 기술해주었다.
 하지만 여기서도 수정항목에 대해 설명이 아니라 회원가입의 기본항목을 복사하여 옴겨 놓은 수준이다.
 여기 문제사항은 아래와 같다.
 1.변경할수 아이디 항목이 변경항목에 나와 있음
 2.이름, 성별, 시민권, 국적에 대한 변경시 유의사항 없음
 3.본인 확인후 이름, 성별, 시민권, 국적을 재변경 하는것에 대한 내용없음
   - 본인확인 이후에도 이름,성별 등을 변경할수 있다면 본인확인에 대한 의미가 없다.
 4.비밀번호는 입력칸을 주어서 수정되어야 된다고 나와있는데 내용이 부족하다.
   - 어느곳에 어떤 입력칸을 주어야하는 지에 대한 내용이 없다.



 */

