package com.kata.shopping;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Tests d'architecture : vérifient que les règles de l'architecture hexagonale sont respectées.
 * Ces tests échouent à la compilation si une dépendance interdite est introduite.
 */
@AnalyzeClasses(
        packages = "com.kata.shopping",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalArchitectureTest {

    private static final String DOMAIN = "com.kata.shopping.domain..";
    private static final String APPLICATION = "com.kata.shopping.application..";
    private static final String INFRASTRUCTURE = "com.kata.shopping.infrastructure..";
    private static final String SPRING_WEB = "org.springframework.web..";

    @ArchTest
    static final ArchRule domainShouldNotDependOnInfrastructure =
            noClasses().that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAPackage(INFRASTRUCTURE)
                    .because("Le domaine ne doit pas connaître l'infrastructure");

    @ArchTest
    static final ArchRule domainShouldNotDependOnApplication =
            noClasses().that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAPackage(APPLICATION)
                    .because("Le domaine ne doit pas connaître la couche application");

    @ArchTest
    static final ArchRule domainShouldNotDependOnSpringWeb =
            noClasses().that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAPackage(SPRING_WEB)
                    .because("Le domaine ne doit pas dépendre de Spring Web");

    @ArchTest
    static final ArchRule applicationShouldNotDependOnInfrastructure =
            noClasses().that().resideInAPackage(APPLICATION)
                    .should().dependOnClassesThat().resideInAPackage(INFRASTRUCTURE)
                    .because("La couche application ne doit pas connaître l'infrastructure");
}
