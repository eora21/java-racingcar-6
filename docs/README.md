# 구현할 기능 목록
- [x] 경주할 자동차 이름을 입력받는다.
	- [x] 입력 요구 메시지를 출력한다.
	- [x] 입력받은 이름들을 구분자 기준으로 나눠 반환한다.
		- [x] 자동차 이름은 쉼표(,)를 기준으로 구분한다.
		- [x] 이름은 5자 이하만 가능하다.
- [x] 사용자는 몇 번의 이동을 할 것인지를 입력할 수 있어야 한다.
	- [x] 입력 요구 메시지를 출력한다.
	- [x] 입력받은 값을 정수 값으로 변환하여 반환한다.
- [x] 자동차들을 전진시킨다.
	- [x] 주어진 횟수 동안 n대의 자동차는 전진 또는 멈출 수 있다. 전진하는 조건은 0에서 9 사이에서 무작위 값을 구한 후 무작위 값이 4 이상일 경우이다(무작위 값은 `camp.nextstep.edu.missionutils.Randoms`의 `pickNumberInRange()`를 활용한다).
- [x] 실행 결과를 출력한다.
	- [x] 전진하는 자동차를 출력할 때 자동차 이름을 같이 출력한다.
	- [x] 지금까지 전진한 횟수를 특정 문자를 이용해 표기한다.
	- [x] 자동차 경주 게임을 완료한 후 누가 우승했는지를 알려준다. 우승자는 한 명 이상일 수 있다.
		- [x] 우승자가 여러 명일 경우 쉼표(,)를 이용하여 구분한다.
- [x] 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`을 발생시킨 후 애플리케이션은 종료되어야 한다.
# 고민한 사항들
## 자동차 객체를 전진시키기
자동차 객체를 전진시키려면 무작위 값을 구한 후 4 이상인지 파악해야 한다.
자동차 객체 밖에서 랜덤한 값을 계산하고, 해당 결과에 따라 자동차를 전진시킬 것인가?
혹은 자동차 객체 스스로가 내부적으로 랜덤한 값을 도출한 후 스스로 전진할 것인가?
### 무작위 값을 이용하는 것은 해당 게임의 룰이자 자동차의 규칙이다
다른 객체에서 자동차를 사용할 때, 두 가지 경우로 나눌 수 있을 것 같다.
- 자동차 전진 룰이 바뀌는 경우(ex: 랜덤값 중 7 이상이 나와야 전진)
- 룰이 필요 없이 전진만 하는 자동차가 필요한 경우(전진 룰 자체가 바뀌는 경우)

위와 같은 문제를 해결하기 위해 자동차 객체를 만들 때 특정 룰을 넣어주는 게 추후 해당 객체를 재사용하기 좋을 것 같다는 판단이 들었다.
### 그런데, 정말 외부에서 주입받도록 작성하는 게 맞을까?
다른 객체에서 차량 객체를 사용할 지 말지는 요구사항에 있는 부분이 아니다. 어디까지나 '추후 그렇게 될 수도 있지 않을까'하는 내 추론이다.
예전 프로젝트에서 배치를 통한 api 접근이 있을 줄 알고 굉장히 어렵게 설계한 로직이 있었는데, 개발 방향성이 변경되며 배치가 사용되지 않아 로직은 로직대로 어려운데 막상 기능은 굉장히 간단한 구조를 갖게 된 적이 있었다.
따라서 이번에는 과한 설계보다, 책임을 기반으로 역할을 나누는 데 집중해보기로 했다.
역할을 나누다 보면 어떠한 로직이 필요할 지 자연스레 나올 것이고, 이를 구현해보기로 했다.
## 책임을 기반으로 역할을 나눠보기
> 책임 - `누구`에게 어떻게 물어봐야 할 것인가

- 경주할 자동차 이름을 입력받기 - `입출력`
	- 사용자에게 출력을 통해 자동차 이름을 입력받을 수 있도록 알려주기 - `출력`
		- 이름은 쉼표 기준으로 구분 - 어떠한 입력에 따라 기준이 달라질 수 있으므로 `입력`
	- 사용자의 입력을 받기 - `입력`
		- 쉼표 기준으로 구분하기 - 기준은 입력이었으므로 `입력`
- 시도할 횟수 입력받기 - `입출력`
	- 사용자에게 시도할 횟수 입력받을 수 있도록 알려주기 - `출력`
	- 사용자의 입력을 받기 - `입력`
		- 숫자로 변환해서 알려주기 - `입력`
- 자동차 경주 진행하기 - `경주 진행`
	- 입력받은 이름들을 기준으로 자동차들을 만들기 - `자동차 팩터리`
		- 자동차의 이름은 5글자 제한 - `자동차 번호판`
		- 자동차들을 만들기 - `자동차 팩터리`
	- 자동차들을 정해진 횟수만큼 경주시키기 - `레이싱 게임`
		- 0부터 9까지의 값 중 4 이상이 나왔는지 확인하기 - `랜덤 결과 계산기`
		- 확인한 값을 기반으로 자동차 전진시키기 - 자동차 팩터리를 통해 만들어진 `자동차 목록`이 랜덤값을 기반으로 각각의 자동차를 움직이게 할 것
		- 자동차들의 현 위치를 파악하기 - `레이싱 게임`
			- 자동차의 위치를 파악하기 - `자동차`에게 현재의 위치를 물어보기
			- 위치들을 정돈하여 반환 - `자동차 목록`
		- 각 순간의 위치들을 정돈하여 반환 - `레이싱 게임`
- 경주 결과를 출력하기 - `입출력`
	- 이번에 출력할 결과가 경주 실행 결과라는 것을 사용자에게 알리기 - `출력`
	- 각 횟수마다의 결과 출력하기 - `출력`
		- 경주 결과 내의 순간의 위치들을 가져오기 - `출력`
		- 순간의 위치들 내의 자동차 위치를 가져오기 - `출력`
		- 자동차 위치를 정해진 형식에 맞게 출력하기 - `출력`
- 최종 우승자 출력하기 - `출력`
	- 정해진 형식에 맞게 출력하기 - 자동차 목록에게 부탁하여 제일 앞선 차의 위치들을 가져와 형식에 맞게 `출력`
### Record를 가볍게 써도 될까?
데이터만 있는 값을 만들기 쉽도록 한 게 Record이다. 내부적으로 데이터만 있는데 hashCode, Equal, toString같은 메서드들이 필요가 없다면 Record로 작성하지 않는 게 좋은 것일까? 아니면 손쉽게 생성할 수도 있고, 추후 해당 메서드들이 필요할 수 있으니 Record로 만들 수 있다면 만드는 게 좋은 것일까?
아니라는 생각이 들어 이번 미션에서는 Record를 사용하지 않았다.
내부적으로 어떤 구성을 지니고 있는가를 확실하게 이해하지 못 한다면 오남용할 가능성이 생긴다. CarStatus가 DTO의 역할을 하고는 있지만 중복 제거가 되선 안된다.
현재 미션에서는 '중복된 차량 이름이 있어선 안 된다'같은 조건이 없다. 나 또한 특별히 해당 규칙을 생각해두진 않았다. 이름이 같은 차량은 있을 수 있다.
이 때, 이름이 같은 차량 5대가 주행횟수마저 같다면 CarStatus 내부 값은 모두 같게 나온다. 이를 중복 제거할 필요가 있을까?
누군가 잘못 생각하여 List 자료구조를 Set으로 구현했다 한들, 5개의 차량은 모두 존재하며 각각의 결과도 다 나와야만 한다. 정말 중복 제거가 필요한 시점이 되어서야 Comparator를 따로 작성하는 게 오남용으로 인한 예상치 못 한 버그를 마주하는 것보다 낫다는 생각이 들었다.
이번주 미션이 끝나면 사람들과 이야기를 나눠봐야겠다.
### 자동차 번호판 객체가 꼭 필요할까?
자동차를 생성할 때 이름이 5자인지 스스로 검증하는 방식과 번호판 객체를 만들 때 검증하는 방식 중 어떤 게 더 좋을까?
자동차 번호판 객체를 생각한 이유는 검증 자체를 자동차 객체가 하지 않도록 하기 위함이었다.
이름의 길이를 검증하는 게 자동차의 책임이 아니라고 생각했기 때문이다.
그러나 이미 String이 불변이고, 이를 검증만 하기 위해 객체를 하나 더 만든 건 오히려 복잡도를 늘리는 게 아닌가 하는 생각이 든다.
따라서 해당 클래스를 삭제한 후, 자동차를 만드는 CarFactory에서 검증하기로 했다.
그러나 이러한 구조는 CarFactory객체를 모른다면 임의의 Car를 만들어낼 수 있고, 글자수 제한이 없는 Car 객체들이 생성될 것이다.
Car 자체에서 name의 길이를 검증하는 방법도 있다. 이래야만 확실히 이름의 길이에 대해 확실한 제한을 둘 수 있다. 그러므로 검증 로직을 Car로 옮기게 되었다.
처음에는 Car가 이름의 길이를 스스로 검증하는 게 책임의 범위를 벗어난다 생각하였는데, 코드 구성에 대해 의구심을 지니다 보니 '내가 내 필드 값을 검증하는 게 왜 내 책임이 아니야?'라는 생각이 들게 되었다.
### 자동차 목록 객체가 꼭 필요할까?
자동차 목록 객체를 생각한 이유는 레이싱 게임 객체와 자동차들을 분리하기 위함이었다.
레이싱 게임 객체는 게임의 로직만을 담고, 자동차 목록은 자동차들을 관리하며 레이싱 게임에서 자동차들을 오남용하는 상황을 막고자 하였다.
레이싱 게임 객체가 경주 횟수 예외 확인(1 이상의 값이 입력되었는가), 주행할 수 있는 규칙(0~9 중 4 이상의 값) 등 경기의 룰에 관해 책임지고, 자동차 목록 객체는 자동차들의 주행 및 상태에 대해 책임지게 되었으므로 자동차 목록 객체는 존재해도 된다는 판단을 내리게 되었다.

### 자동차 목록 생성 클래스가 꼭 필요할까?
자동차 목록을 만들기 위해 자동차 목록 생성 클래스를 사용하였다. 그러나 해당 클래스의 존재를 모른다면 다시금 자동차 목록을 만들기 위한 코드를 누군가가 새로 작성할 수 있으므로, 자동차 목록 생성자를 private로 변경하고 자동차 목록 생성 클래스의 로직을 자동차 목록의 정적 팩터리 메서드 패턴으로 작성하였다.
### 결과에 대해 하나로 묶는 게 좋을까?
경주 후 차량들의 기록과 우승자 도출에 대해 하나의 메서드로 묶는 것이 좋을 지 고민했다. 어떻게 보면 경주 로그를 보는 것, 우승자를 보는 것은 경기 결과를 보는 하나의 로직으로 봐도 무방했기 때문이다. 5바퀴 돌린 후 해당 기록을 출력하고, 추가적으로 3바퀴를 더 돌린 후에 출력을 안했다가 우승자를 조회했을 때 원하던 값이 나오지 않을 수도 있으니, 오남용을 방지할 수도 있고 말이다.
다만 하나로 묶을 시 너무 큰 로직이 하나에 몰리는 것 같았고, 현재 구조에서도 컨트롤러를 통해 각각의 메서드 순서를 맞춰 진행하는 중이므로 오히려 지금과 같이 분리시키는 게 각각의 책임을 잘 표현하는 것으로 느껴져 다른 브랜치에 변경했던 내용들을 담아두기만 하였다.
허나 감상문을 쓰다 보니 오남용을 방지하는 게 우선이며, 게임 결과 출력에 대해서도 우승자 목록 확인이 포함되는 게 더 자연스러우므로 해당 브랜치 내용을 merge했다.

> 고민과 더불어 미리 느낀 점들을 작성하고, 제출 전 녹일 것

- 객체지향에 대해서는 여러 번 학습했었다. 다만 역할과 책임에 대해 깊게 고민해 본 적은 없었다. '나중에 수정하려면 여기에 메서드 작성하는 게 낫겠다', '이건 외부에서 참조하면 안되니까 이곳에 숨겨야지' 등으로 객체지향의 기초는 지키려 노력했었지만, 메서드를 최대한 작게 만들면서 어느 역할이 이를 수행할 것인지 곰곰히 생각해 본 적은 없었다. 시간이 거의 일주일이나 주어진 만큼, 성급히 코드를 작성하기보다 많은 것들을 고려해보려 노력하고 있다.
