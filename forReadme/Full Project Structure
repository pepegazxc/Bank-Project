``` bash
|   .gitattributes
|   .gitignore
|   bank_project.iml
|   env.properties
|   HELP.md
|   mvnw
|   mvnw.cmd
|   pom.xml
|
+---src
|   +---main
|   |   +---java
|   |   |   \---bank_project
|   |   |       |   BankProjectApplication.java
|   |   |       |
|   |   |       +---configuration
|   |   |       |       MvcConfig.java
|   |   |       |       RedisConfig.java
|   |   |       |
|   |   |       +---controller
|   |   |       |       AccountController.java
|   |   |       |       CardController.java
|   |   |       |       HistoryController.java
|   |   |       |       LoginController.java
|   |   |       |       MainController.java
|   |   |       |       RegistrationController.java
|   |   |       |       ReplenishmentController.java
|   |   |       |       ReviewsController.java
|   |   |       |       TechSupportController.java
|   |   |       |       UserController.java
|   |   |       |
|   |   |       +---dto
|   |   |       |   +---cache
|   |   |       |   |       CachedAllUserDto.kt
|   |   |       |   |       CachedUserAccountDto.kt
|   |   |       |   |       CachedUserCardDto.kt
|   |   |       |   |       CachedUserDto.kt
|   |   |       |   |       CachedUserOperationHistoryDto.kt
|   |   |       |   |
|   |   |       |   +---request
|   |   |       |   |   |   AccountRequest.kt
|   |   |       |   |   |   CardRequest.kt
|   |   |       |   |   |   ChangeInfoRequest.kt
|   |   |       |   |   |   LoginRequest.kt
|   |   |       |   |   |   RegistrationRequest.kt
|   |   |       |   |   |
|   |   |       |   |   \---request
|   |   |       |   |       \---transfer
|   |   |       |   |               BetweenAccountsCashRequest.kt
|   |   |       |   |               BetweenUsersCashRequest.kt
|   |   |       |   |               TransferRequest.kt
|   |   |       |   |
|   |   |       |   \---view
|   |   |       |           ViewAccountDto.kt
|   |   |       |           ViewCardDto.kt
|   |   |       |
|   |   |       +---entity
|   |   |       |   |   Accounts.java
|   |   |       |   |   Cards.java
|   |   |       |   |   GoalTemplates.java
|   |   |       |   |   User.java
|   |   |       |   |   UserAccount.java
|   |   |       |   |   UserCard.java
|   |   |       |   |   UserContact.java
|   |   |       |   |   UserOperationHistory.java
|   |   |       |   |
|   |   |       |   \---interfaces
|   |   |       |           BalanceHolder.kt
|   |   |       |
|   |   |       +---exception
|   |   |       |   |   ControllerExceptionHandler.kt
|   |   |       |   |   DtoExceptionHandler.kt
|   |   |       |   |   EntityExceptionHandler.kt
|   |   |       |   |   TokenExceptionHandler.kt
|   |   |       |   |   TransferMoneyExceptionHandler.kt
|   |   |       |   |
|   |   |       |   \---custom
|   |   |       |           AccountsNotFoundException.kt
|   |   |       |           AmountTransferException.kt
|   |   |       |           CardsNotFoundException.kt
|   |   |       |           ControllerException.kt
|   |   |       |           EmptyDtoException.kt
|   |   |       |           GoalTemplatesNotFoundException.kt
|   |   |       |           IncorrectPasswordException.kt
|   |   |       |           InsufficientBalanceException.kt
|   |   |       |           RecipientNotFoundException.kt
|   |   |       |           TokenVerificationException.kt
|   |   |       |           TransferMoneyException.kt
|   |   |       |           UserAccountNotFoundException.kt
|   |   |       |           UserCardNotFoundException.kt
|   |   |       |           UserNotFoundException.kt
|   |   |       |           UserOperationHistoryNotFoundException.kt
|   |   |       |
|   |   |       +---mapper
|   |   |       |       UserAccountMapper.java
|   |   |       |       UserCardMapper.java
|   |   |       |       UserHistoryMapper.java
|   |   |       |       UserMapper.java
|   |   |       |
|   |   |       +---repository
|   |   |       |   +---jpa
|   |   |       |   |       AccountRepository.java
|   |   |       |   |       CardRepository.java
|   |   |       |   |       GoalTemplateRepository.java
|   |   |       |   |       TokenRepository.java
|   |   |       |   |       UserAccountRepository.java
|   |   |       |   |       UserCardRepository.java
|   |   |       |   |       UserHistoryRepository.java
|   |   |       |   |       UserRepository.java
|   |   |       |   |
|   |   |       |   \---redis
|   |   |       |           CachedUserHistoryRepository.java
|   |   |       |           UserInfoRepository.java
|   |   |       |
|   |   |       +---security
|   |   |       |   +---security
|   |   |       |   |   \---configuration
|   |   |       |   |           CipherConfig.java
|   |   |       |   |           SecurityConfig.java
|   |   |       |   |
|   |   |       |   \---token
|   |   |       |           SessionToken.java
|   |   |       |
|   |   |       \---service
|   |   |               AccountService.java
|   |   |               AuthContextService.java
|   |   |               CardService.java
|   |   |               CipherService.java
|   |   |               OperationHistoryService.java
|   |   |               RedisService.java
|   |   |               SessionTokenService.java
|   |   |               TransferService.java
|   |   |               UserService.java
|   |   |
|   |   \---resources
|   |       |   application.properties
|   |       |
|   |       +---db
|   |       |   \---migration
|   |       |           V1__init_migration.sql
|   |       |           V2_change_fields_types_and_names.sql
|   |       |           V3_fill_fields_in_tables_card_and_account.sql
|   |       |           V4_change_field_types.sql
|   |       |           V5_fill_fields_in_table_goal_templates.sql
|   |       |
|   |       +---static
|   |       |   +---icons
|   |       |   |       account-icon.png
|   |       |   |       bank-deposit-icon.png
|   |       |   |       benefits-account-icon.png
|   |       |   |       burger-menu-icon.png
|   |       |   |       card-icon.png
|   |       |   |       check-mark-icon.png
|   |       |   |       deposit-account-icon.png
|   |       |   |       history-icon.png
|   |       |   |       order-card.png
|   |       |   |       reviews-icon.png
|   |       |   |       right-arrow.png
|   |       |   |       save-account-icon.png
|   |       |   |       settings-icon.png
|   |       |   |       support-icon.png
|   |       |   |       theme-icon.png
|   |       |   |       user-icon.png
|   |       |   |
|   |       |   \---style
|   |       |       +---account-order-blank-style
|   |       |       |       account-blank-style.css
|   |       |       |
|   |       |       +---card-order-blank-style
|   |       |       |       card-blank-style.css
|   |       |       |
|   |       |       +---card-order-page-style
|   |       |       |       card-info-expand.js
|   |       |       |       card-order-style.css
|   |       |       |
|   |       |       +---global-style
|   |       |       |       burger-menu-style.css
|   |       |       |       burger-menu.js
|   |       |       |       footer-script.js
|   |       |       |       footer-style.css
|   |       |       |       header-style.css
|   |       |       |
|   |       |       +---history-page-style
|   |       |       |       history-page-style.css
|   |       |       |
|   |       |       +---login-page-style
|   |       |       |       login-style.css
|   |       |       |
|   |       |       +---main-page-style
|   |       |       |       news-expand.js
|   |       |       |       news-style.css
|   |       |       |
|   |       |       +---new-account-page-style
|   |       |       |       new-account-page-style.css
|   |       |       |       new-account-script.js
|   |       |       |
|   |       |       +---registration-page-style
|   |       |       |       registration-page-style.css
|   |       |       |
|   |       |       +---replenishment-page-style
|   |       |       |       replenishment-page-style.css
|   |       |       |
|   |       |       +---reviews-page-style
|   |       |       |       reviews-style.css
|   |       |       |
|   |       |       +---support-page-style
|   |       |       |       support-page-style.css
|   |       |       |       support-script.js
|   |       |       |
|   |       |       \---user-page-style
|   |       |               card-data.js
|   |       |               user-info-style.css
|   |       |
|   |       \---templates
|   |               account-order-blank.html
|   |               card-order-blank-type-1.html
|   |               card-order-blank-type-2.html
|   |               card-order-blank-type-3.html
|   |               card-order-blank-type-4.html
|   |               card-order-blank.html
|   |               card-order-page.html
|   |               edit-user-info-page.html
|   |               history-page.html
|   |               login-page.html
|   |               main-page.html
|   |               new-account-page.html
|   |               registration-page.html
|   |               replenishment-page.html
|   |               reviews-page.html
|   |               support-page.html
|   |               user-page.html
|   |
|   \---test
|       \---java
|           \---bank_project
|               |   BankProjectApplicationTests.java
|               |
|               +---account
|               |   \---test
|               |           AccountServiceTest.java
|               |
|               +---card
|               |   \---test
|               |           CardServiceTest.java
|               |
|               \---transfer
|                   \---test
|                           TransferServiceTest.java
```
