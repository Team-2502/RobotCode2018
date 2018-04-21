package com.team2502.robot2018.command.autonomous.ingredients;

/**
 * Let drive team choose what they want to achieve in auto
 * <p>
 * TODO: Implement multi-cube autos
 */
public enum AutonStrategy
{
    SWITCH("Switch"),
    VAULT_2_SWITCH("Vault-Switch-Switch"),
    SWITCH_TWICE("Switch-Switch"), // Used for center
    SCALE("Scale"),
    SCALE_TWICE("Scale-Scale"),
    SCALE_SWITCH("Scale-Switch"),
    STRAIGHT("Straight"),
    FCC("FCC"),
    TEST_SECOND_CUBE("Test only second cube");

    private final String name;

    AutonStrategy(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
