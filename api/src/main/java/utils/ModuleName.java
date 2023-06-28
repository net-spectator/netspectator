package utils;

public class ModuleName {
    private static volatile ModuleName moduleName;

    private static volatile String name;

    private ModuleName() {
    }

    public static synchronized ModuleName getModuleName() {
        ModuleName mn = moduleName;
        if (mn == null) {
            mn = moduleName;
            if (mn == null) {
                moduleName = mn = new ModuleName();
            }
        }
        return mn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        ModuleName.name = name;
    }
}
