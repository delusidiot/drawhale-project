# Drawhale Project

MSA로 구성된 프로젝트 입니다.

백엔드의 설정 정보는 모두 Cloud Config 서버에서 설정정보를 가지고와서 RabbitMQ로 다른 서비스가 가져오기 때문에 해당 프로젝트만 받아 바로 실행하면 제대로 실행되지 않을것입니다.

## 주요 기술 목록

### BackEnd

- Spring Boot

- Spring Data

  - Spring Data JPA

- Spring Cloud

  - Spring Cloud Bus
  - Spring Cloud Config
  - Spring Cloud Gateway

- SpringSecurity

- Spring HATEOAS

### FonrtEnd

- Next.js 13

### Infra

- AWS EKS
- AWS ROUTE53
