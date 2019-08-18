# # Task 1

----------------

* ### 이해 및 습득해야  할  지식

  1. **SSH**

     ```
     시큐어 셸(Secure Shell, SSH)은 네트워크 상의 다른 컴퓨터에 로그인하거나 원격 시스템에서 명령을 실행하고 다른 시스템으로 파일을 복사할 수 있도록 해 주는 응용 프로그램 또는 그 프로토콜을 가리킨다.
     ```

  2. **SFTP**

     ```
     SSH 파일 전송 프로토콜(SSH File Transfer Protocol) 또는 보안 파일 전송 프로토콜(Secure File Transfer Protocol, SFTP)은 신뢰할 수 있는 데이터 스트림을 통해 파일 접근, 파일 전송, 파일 관리를 제공하는 네트워크 프로토콜이다.
     ```

  3. **REST**

     ```
     1) “Representational State Transfer” 의 약자.
     2) 자원을 이름(자원의 표현)으로 구분하여 해당 자원의 상태(정보)를 주고 받는 모든 것을 의미한다.
        즉, 자원(resource)의 표현(representation) 에 의한 상태 전달이다.
     	a) 자원(resource)의 표현(representation)
     		- 자원: 해당 소프트웨어가 관리하는 모든 것
     		  -> Ex) 문서, 그림, 데이터, 해당 소프트웨어 자체 등
     		- 자원의 표현: 그 자원을 표현하기 위한 이름
     		  -> Ex) DB의 학생 정보가 자원일 때, ‘students’를 자원의 표현으로 정한다.
     	b) 상태(정보) 전달
     		- 데이터가 요청되어지는 시점에서 자원의 상태(정보)를 전달한다.
     		- JSON 혹은 XML를 통해 데이터를 주고 받는 것이 일반적이다.
     
     3) REST는 기본적으로 웹의 기존 기술과 HTTP 프로토콜을 그대로 활용하기 때문에 웹의 장점을 최대한 
        활용할 수 있는 아키텍처 스타일이다.
     4) REST는 네트워크 상에서 Client와 Server 사이의 통신 방식 중 하나이다.
     5) (중요) HTTP URI(Uniform Resource Identifier)를 통해 자원(Resource)을 명시하고, HTTP 
        Method(POST, GET, PUT, DELETE)를 통해 해당 자원에 대한 CRUD Operation을 적용하는 것을 의미한다.
     6) CRUD Operation
     	Create : 생성(POST)
     	Read : 조회(GET)
     	Update : 수정(PUT)
     	Delete : 삭제(DELETE)
     	HEAD: header 정보 조회(HEAD)
     7) REST 특징
     	1. Server-Client(서버-클라이언트 구조)
     		- 자원이 있는 쪽이 Server, 자원을 요청하는 쪽이 Client가 된다.
     			- REST Server: API를 제공하고 비즈니스 로직 처리 및 저장을 책임진다.
     			- Client: 사용자 인증이나 context(세션, 로그인 정보) 등을 직접 관리하고 책임진다.
     		- 서로 간 의존성이 줄어든다.
     	2. Stateless(무상태)
     		- HTTP 프로토콜은 Stateless Protocol이므로 REST 역시 무상태성을 갖는다.
     		- Client의 context를 Server에 저장하지 않는다.
     			즉, 세션과 쿠키와 같은 context 정보를 신경쓰지 않아도 되므로 구현이 단순해진다.
     		- Server는 각각의 요청을 완전히 별개의 것으로 인식하고 처리한다.
     			각 API 서버는 Client의 요청만을 단순 처리한다.
     			즉, 이전 요청이 다음 요청의 처리에 연관되어서는 안된다.
     			물론 이전 요청이 DB를 수정하여 DB에 의해 바뀌는 것은 허용한다.
     			Server의 처리 방식에 일관성을 부여하고 부담이 줄어들며, 서비스의 자유도가 높아진다.
     	3. Cacheable(캐시 처리 가능)
     		- 웹 표준 HTTP 프로토콜을 그대로 사용하므로 웹에서 사용하는 기존의 인프라를 그대로 활용할 수 
     		  있다.
     			- 즉, HTTP가 가진 가장 강력한 특징 중 하나인 캐싱 기능을 적용할 수 있다.
     			- HTTP 프로토콜 표준에서 사용하는 Last-Modified 태그나 E-Tag를 이용하면 
     			  캐싱 구현이 가능하다.
     		- 대량의 요청을 효율적으로 처리하기 위해 캐시가 요구된다.
     		- 캐시 사용을 통해 응답시간이 빨라지고 REST Server 트랜잭션이 발생하지 않기 때문에 전체 
     		  응답
     		시간, 성능, 서버의 자원 이용률을 향상시킬 수 있다.
     	4. Layered System(계층화)
     		- Client는 REST API Server만 호출한다.
     		- REST Server는 다중 계층으로 구성될 수 있다.
     			- API Server는 순수 비즈니스 로직을 수행하고 그 앞단에 보안, 로드밸런싱, 암호화, 
     			  사용자 인증 등을 추가하여 구조상의 유연성을 줄 수 있다.
     			- 또한 로드밸런싱, 공유 캐시 등을 통해 확장성과 보안성을 향상시킬 수 있다.
     		- PROXY, 게이트웨이 같은 네트워크 기반의 중간 매체를 사용할 수 있다.
     	5. Code-On-Demand(optional)
     		- Server로부터 스크립트를 받아서 Client에서 실행한다.
     		- 반드시 충족할 필요는 없다.
     	6. Uniform Interface(인터페이스 일관성)
     		- URI로 지정한 Resource에 대한 조작을 통일되고 한정적인 인터페이스로 수행한다.
     		- HTTP 표준 프로토콜에 따르는 모든 플랫폼에서 사용이 가능하다.
     			- 특정 언어나 기술에 종속되지 않는다.
     ```

  4. **REST AP**I

     ```
     API(Application Programming Interface) 란?
     	- 데이터와 기능의 집합을 제공하여 컴퓨터 프로그램간 상호작용을 촉진하며, 서로 정보를 교환가능 하
     	  도록 하는 것
     REST API 란?
     	- REST 기반으로 서비스 API를 구현한 것
     	- 최근 OpenAPI(누구나 사용할 수 있도록 공개된 API: 구글 맵, 공공 데이터 등), 마이크로 서비스
     	  (하나의 큰 애플리케이션을 여러 개의 작은 애플리케이션으로 쪼개어 변경과 조합이 가능하도록 만든
           아키텍처) 등을 제공하는 업체 대부분은 REST API를 제공한다.
     REST API 의 특징
     	- 사내 시스템들도 REST 기반으로 시스템을 분산해 확장성과 재사용성을 높여 유지보수 및 운용을 
     	  편리하게 할 수 있다.
     	- REST는 HTTP 표준을 기반으로 구현하므로, HTTP를 지원하는 프로그램 언어로 클라이언트, 
     	  서버를 구현할 수 있다.
     	- 즉, REST API를 제작하면 델파이 클라이언트 뿐 아니라, 자바, C#, 웹 등을 이용해 클라이언트를 
     	  제작할 수 있다.
     ```

  5. **RESTful**

     ```
     RESTful 이란?
     	- RESTful은 일반적으로 REST라는 아키텍처를 구현하는 웹 서비스를 나타내기 위해 사용되는 용어이다.
     	- ‘REST API’를 제공하는 웹 서비스를 ‘RESTful’하다고 할 수 있다.
     	- RESTful은 REST를 REST답게 쓰기 위한 방법으로, 누군가가 공식적으로 발표한 것이 아니다.
     	- 즉, REST 원리를 따르는 시스템은 RESTful이란 용어로 지칭된다.
     RESTful의 목적
     	- 이해하기 쉽고 사용하기 쉬운 REST API를 만드는 것
     	- RESTful한 API를 구현하는 근본적인 목적이 성능 향상에 있는 것이 아니라 일관적인 컨벤션을 통한
            API의 이해도 및 호환성을 높이는 것이 주 동기이니, 성능이 중요한 상황에서는 굳이 RESTful한 
            API를 구현할 필요는 없다.
     ```
     
  6. **CSV 파일**
  
     ```
     CSV(영어: comma-separated values)는 몇 가지 필드를 쉼표(,)로 구분한 텍스트 데이터 및 텍스트 파일이다.
     ```
  
  7. **Oracle VM virtualBox** 
  
     ```
     - 오라클에서 개발한 크로스 플랫폼 가상화 프로그램.
     - 원도우즈 호스트 OS와 우분투 게스트 OS를 설치하여 2개의 운영체제를 큰 영향없이 동시에 이용.
     ```
  
  8. **SSH 와 sFTP의 관계**
  
     ```
     SFTP(Secure File transfer protocol) - SSH와 마찬가지로 전송시 암호화시켜서 전송을 하게 되는데 그 중에 FTP와 같이 파일을 전송할 때 암호화 시켜서 전송합니다.
     
     SSH에 부가적으로 있는 기능들 중 하나의 기능입니다. FTP와 같은 역할을 하지만, 사용하는 포트는 FTP 포트(기본 21포트) 가 아닌 SSH 접속시 사용하는 포트를 사용하고, SSH 접속 계정으로 로그인 할 수 있는 FTP 입니다.
     
     즉, FTP의 형태를 가지고 있지만 그 안에 내용물은 SSH를 이용해서 연결을 하는 것이 바로 sFTP입니다. (계정 정보와 접속 포트가 SSH와 같습니다.)
     
     실제로도 접속은 SSH 접속 프로그램을 이용해서 접속하는 것이 아니라 일반적으로 FTP Client 프로그램을 통해 접속을 합니다. 일반 계정을 사용하는 경우에는 이 sFTP가 큰 메리트는 없어 보입니다.
     
     다만, 사용자가 root 인경우에는 굉장히 유용합니다. 기본적으로 Linux Server 버전들의 특성상 스크립트 명령어들은 필수적이며, 파일을 알아보는데에도 명령어를 사용합니다. 일반적으로 많이 쓰는 경로를 찾아가서 관련 파일들을 확인하는 것은 어렵지는 않지만, 윈도우에 익숙한 분들에게는 그 조차도 힘들일이 될 수 있습니다.
     
     sFTP를 그냥 FTP를 비교했을 떄 장점
     	1. 파일 업/다운로드가 쉬워집니다. SSH로 접속하는 경우보다 업/다운로드가 쉬워집니다.
     	2. SSH 기반이기 때문에 퍼미션 변경에서 더 강력해 집니다. (특히 root 계정일 때.)
     	3. 외부에서도 파일관리가 편해집니다.
     	4. 서버의 시스템 관리가 더 편리해집니다. (root 계정일 경우)
     
     그래서 sSFTP 라는 특별한 기능을 이용하면, FTP와 같이 사용이 가능합니다. (다만, FTP 접속 프로그램이 sFTP 기능을 지원해야 합니다.) 
     ```
  
     

------------

* ### 과제  내용

  * (가정) 로컬인  client가 다른 원격 컴퓨터(Virtual Box's ubuntu)에게 어떤 것을 요구하면 그 요구로부터 어떠한 파일을 생성한다. 하지만 그 파일이 생성되기 까지 시간이 얼마나 걸릴지 모르는 일이라고 해보자. 시간이 지나 언젠가 파일이 생성될 것이고 그 생성되었음을 REST API  서버에게 알려주고 서버는 다시 원격컴퓨터에게 그 파일을 다운받겠다는  신호를 보내고 다운로드가 이루어지게 된다. 

------

* ### 날짜별 진행 상황

  - 2019.08.14.Wed

    1. **Window10** 부터는 putty 없이 원격지의 리눅스 터미널에 접속하기 위해, **openssh client**와 **server**의 설치로 

       가능해졌음을 알게 되었다. 

    2. Oracle VM virtualBox를 설치했고 ubuntu OS의  iso 파일을 인식시켜 가상으로 사용할 수  있도록 했다.

    3. 호스트 OS의 cmd나 powershell 에서 ssh **[게스트 OS의 사용자 이름] + @ + [게스트 OS의 접속서버주소]** 를 통해 원격지인 게스트에 접속했다. 접속 종료는 **exit**.

  

  - 2019.08.15.THU
    1. **커맨드 라인으로 sftp 명령어 실습**을 통한 이해. **[ put | get ] + [file name]**
       - 파일이름이 #으로 시작하는 것을 put으로 원격  시스템으로 복사하려고 했는데, 인식을 못하더라...
       - 파일이 복사될 곳과  복사할 파일이 존재하는 곳으로 각각 cd 와 lcd를 통해 이동해서 한다.
    2. intelliJ 웹 프로젝트를 생성했다.
  - 2019.08.16.FRI
    1. Client.java : JSch(Java Secure Channel) 라이브러리를 이용한 sFTP 원격 연결부, 연결해지부 구현.
  - 2019.08.17.SAT
    1. JSch API에 대한 공부
       - JSch, Session, Channel
    2. 클래스 이름을 전체적으로 바꿈. 조금 더 객체지향적으로 내부 코딩을 했음. (부잠님께서 지적하신 내용)
    3. MVC 패턴으로 프로젝트 구조  수정. 솔직히 필요없는데 굳이 했음. 매우 단순한 MVC 패턴임.
  - 2019.08.18.SUN
    1. remote shell 커맨드라인에 입력후 출력되는 부분 수정함.  실제 ubuntu shell을 사용하는 것처럼 하기 위해...
    2. 
----------------------

* ### 참고 사이트

  * https://extrememanual.net/12589 <OpenSSH Client 설치 및 이용>
  
  * [https://zetawiki.com/wiki/%EC%9A%B0%EB%B6%84%ED%88%AC_sshd_%EC%84%A4%EC%B9%98](https://zetawiki.com/wiki/우분투_sshd_설치) <OpenSSH Server 설치 및 이용>
  
  * https://iamaman.tistory.com/2568 <OpenSSH C, S 설치 및 이용>
  
  * [https://webisfree.com/2017-08-02/linux-%EC%89%98%EC%97%90%EC%84%9C-ip-%EC%A3%BC%EC%86%8C-%ED%99%95%EC%9D%B8%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95](https://webisfree.com/2017-08-02/linux-쉘에서-ip-주소-확인하는-방법) <linux에서 ip 주소 확인하는 방법>
  
  * https://m.blog.naver.com/wideeyed/220960764870 <오라클 VirtualBox에 우분투(ubuntu) 설치하기 >
  
  * https://extrememanual.net/8257 <버추얼박스  클립보드 공유 설정(복사, 붙여넣기)>
  
  * [https://falsy.me/%EA%B0%84%EB%8B%A8%ED%95%98%EA%B2%8C-%EC%9E%90%EC%A3%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94-ssh-%EB%AA%85%EB%A0%B9%EC%96%B4%EB%A5%BC-%EC%A0%95%EB%A6%AC%ED%95%A9%EB%8B%88%EB%8B%A4/](https://falsy.me/간단하게-자주-사용하는-ssh-명령어를-정리합니다/) 
  
    <간단하게 자주 사용하는 SSH  명령어>
  
  * [https://nikemaniya.wordpress.com/2014/01/22/linux-ssh-%EC%A0%91%EC%86%8D-%EB%AA%85%EB%A0%B9%EC%96%B4/](https://nikemaniya.wordpress.com/2014/01/22/linux-ssh-접속-명령어/) <ssh 접속 명령어>
  
  * https://studyforus.tistory.com/236 <SFTP의 뜻과 사용하는 방법> [중요개념]
  
  * https://docs.oracle.com/cd/E37933_01/html/E36613/remotehowtoaccess-14.html <원격 시스템에 로그인하여 파일 복사 sftp>
  
  * https://docs.oracle.com/cd/E37933_01/html/E36613/remotehowtoaccess-14.html#remotehowtoaccess-40629
  
    <원격시스템에  대한 sftp 연결을 열고 닫는 방법>
    
  * https://altkeycode.tistory.com/17 <인텔리제이 web 프로젝트 시작하기  maven>
  
  * https://digitalbourgeois.tistory.com/58 <JAVA : HttpClient로 REST API 호출하기>
  
  * https://gs.saro.me/dev?tn=581 <자바 리눅스 SSH 사용하기>
  
  * https://gs.saro.me/dev?tn=574 <자바 : FTP , SFTP , ,FTPS 사용하기>
  
  * https://steemit.com/kr-dev/@capslock/java-sftp-using-jsch-sftp <Java SFTP Using Jsch - 자바로 SFTP 파일 업로드/다운로드 개발>
  
  * https://www.leafcats.com/177 <[JSch 라이브러리 : java 에서 원격 ssh 명령어 실행]>
  
  * http://www.jcraft.com/jsch/examples/Shell.java.html <JSch 공식 사이트>
  
  * https://epaul.github.io/jsch-documentation/javadoc/ <JSch documentation>
  
  * https://epaul.github.io/jsch-documentation/javadoc/com/jcraft/jsch/Session.html <Session documentation>
  
  * https://epaul.github.io/jsch-documentation/simple.javadoc/com/jcraft/jsch/Channel.html <Channel documentation>
  