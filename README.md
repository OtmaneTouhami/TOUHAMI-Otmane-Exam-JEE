# Credit Management System

This document provides a comprehensive overview of the Credit Management System project, including the architecture, class diagram, and detailed explanations of both the backend and frontend components.

## System Architecture

![Architecture Diagram](/scrrenshots/architecture.png)

## Class Diagram

![Class Diagram](/scrrenshots/class-diagram.png)

## Backend Implementation

The backend of the Credit Management System is built using Spring Boot, providing a RESTful API for managing client credits and repayments. It follows a layered architecture with clear separation of concerns.

### Technology Stack

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security with JWT**
- **MariaDB**

### Database Configuration

The application is configured to use MariaDB as the database with the following properties:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/credit_management_db?createDatabaseIfNotExist=true
spring.datasource.username=otmane
spring.datasource.password=Slme@1234
spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
```

The application is set to run on port 8080.

### Domain Model

The domain model consists of the following key entities:

#### 1. Client

```java
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Credit> credits;
}
```

The `Client` entity represents a customer in the system who can apply for credits.

#### 2. Credit (Abstract Base Class)

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "credit_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateDemande;
    @Enumerated(EnumType.STRING)
    private StatutCredit statut;
    private LocalDate dateAcceptation;
    private BigDecimal montant;
    private Integer duree;
    private BigDecimal tauxInteret;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Remboursement> remboursements;
}
```

`Credit` is an abstract base class using Single Table Inheritance pattern to represent different types of credits.

#### 3. Credit Subtypes

The system supports three types of credits:

- **CreditPersonnel**: Personal credit for individual needs
- **CreditImmobilier**: Real estate credit for property purchases
- **CreditProfessionnel**: Business credit for professional activities

Each type has specific attributes relevant to its purpose.

#### 4. Remboursement (Repayment)

```java
@Entity
@Table(name = "remboursements")
public class Remboursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private BigDecimal montant;
    @Enumerated(EnumType.STRING)
    private TypeRemboursement type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id")
    private Credit credit;
}
```

The `Remboursement` entity represents individual repayments made for a credit.

### Enumerations

The system uses several enumerations to represent different states and types:

- **StatutCredit**: Represents the status of a credit application (EN_COURS, ACCEPTE, REJETE)
- **TypeRemboursement**: Represents the type of repayment
- **TypeBienFinance**: Represents the type of property financed in real estate credits

### DTO Layer

The application follows the DTO pattern to transfer data between the client and the server:

- **ClientDTO**: Represents client data
- **CreditDTO**: Represents credit data with all necessary fields for different credit types
- **RemboursementDTO**: Represents repayment data

### Service Layer

The service layer contains the business logic of the application:

- **CreditManagementService**: Interface defining operations for credit management
- **CreditManagementServiceImpl**: Implementation of credit management operations

### Controller Layer

The REST controllers expose the API endpoints for clients, credits, and repayments:

- **ClientRestController**: Handles client-related operations
- **CreditRestController**: Handles credit-related operations
- **RemboursementRestController**: Handles repayment-related operations

Each controller includes proper authorization rules using Spring Security's `@PreAuthorize` annotations to restrict access based on user roles.

### Security

The application uses Spring Security with JWT (JSON Web Token) for authentication and authorization. The security configuration is defined in `SecurityConfig.java`.

## Frontend Implementation

The frontend of the Credit Management System is built using Angular, providing a responsive and user-friendly interface for different types of users.

### Technology Stack

- **Angular 17**
- **TypeScript**
- **HTML/CSS**

### Application Structure

The frontend follows a modular architecture with feature-based organization:

#### Core Components

- **App Component**: Root component of the application
- **Navbar Component**: Navigation bar displayed across the application

#### Authentication

- **Login Component**: Handles user login
- **Auth Service**: Manages authentication state, JWT token handling, and user roles
- **Auth Guard**: Protects routes based on user authentication and roles

#### DTOs (Data Transfer Objects)

The frontend uses TypeScript interfaces to represent the data received from the backend:

```typescript
// Client DTO
export interface ClientDTO {
  id?: number;
  nom: string;
  email: string;
}

// Credit DTO
export interface CreditDTO {
  id?: number;
  dateDemande: string;
  statut: StatutCredit;
  dateAcceptation?: string;
  montant: number;
  duree: number;
  tauxInteret: number;
  clientId: number;
  type: string;
  motif?: string;
  typeBienFinance?: TypeBienFinance;
  raisonSocialeEntreprise?: string;
}
```

#### User Role-Based Features

1. **Admin Features**:

   - Admin dashboard for system overview
   - Client management
   - Credit management

2. **Employee Features**:

   - Employee dashboard
   - Client list management
   - Credit tasks handling

3. **Client Features**:
   - Client dashboard
   - Apply for credit functionality
   - View and manage personal credits

### Services

The frontend includes several services to handle communication with the backend API:

- **AuthService**: Handles authentication and user management
- **ClientService**: Manages client-related API calls
- **CreditService**: Manages credit-related API calls

### HTTP Interceptor

The application uses an HTTP interceptor to automatically attach the JWT token to outgoing requests:

```typescript
@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    // Add authorization header with JWT token if available
    if (this.authService.isAuthenticated()) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.authService.getAccessToken()}`,
        },
      });
    }
    return next.handle(request);
  }
}
```

## Conclusion

The Credit Management System is a comprehensive solution for managing credit applications, approvals, and repayments. It provides different interfaces for clients, employees, and administrators, with appropriate access controls and security measures. The system demonstrates the use of modern software architecture practices, including layered architecture, dependency injection, and separation of concerns.
