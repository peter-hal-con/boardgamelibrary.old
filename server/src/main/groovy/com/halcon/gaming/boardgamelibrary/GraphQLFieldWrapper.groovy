package com.halcon.gaming.boardgamelibrary

import graphql.language.Argument
import graphql.language.Document
import graphql.language.Field
import graphql.language.ObjectField
import graphql.language.ObjectValue
import graphql.language.Value

class GraphQLFieldWrapper {
    private final String fieldName
    private final Set<String> selectionSet
    private final Map<String, Value> argumentMap

    private GraphQLFieldWrapper(String fieldName, Set<String> selectionSet, Map<String, Value> argumentMap) {
        this.fieldName = fieldName
        this.selectionSet = selectionSet
        this.argumentMap = argumentMap
    }

    public String getFieldName() {
        return fieldName
    }

    public Set<String> getSelectionSet() {
        return selectionSet
    }

    public Map<String, Value> getArgumentMap() {
        return argumentMap
    }

    private static Set<String> collectSelectionSet(String prefix, List<Field> fields) {
        Set<String> retval = new HashSet<String>()

        for(Field field : fields) {
            if(field.selectionSet != null) {
                retval.addAll(collectSelectionSet(prefix + field.name + ".", field.selectionSet.children))
            } else {
                retval.add(prefix + field.name)
            }
        }

        return retval
    }

    private static Map<String, Value> collectObjectFields(String prefix, List<ObjectField> arguments) {
        Map<String, Value> retval = [:]

        for(ObjectField argument : arguments) {
            if(argument.value instanceof ObjectValue) {
                retval.putAll(collectObjectFields(prefix + argument.name + ".", argument.value.getObjectFields()))
            } else {
                retval[prefix + argument.name] = argument.value
            }
        }

        return retval
    }

    private static Map<String, Value> collectArgumentMap(String prefix, List<Argument> arguments) {
        Map<String, Value> retval = [:]

        for(Argument argument : arguments) {
            if(argument.value instanceof ObjectValue) {
                retval.putAll(collectObjectFields(prefix + argument.name + ".", argument.value.getObjectFields()))
            } else {
                retval[prefix + argument.name] = argument.value
            }
        }

        return retval
    }

    public static GraphQLFieldWrapper wrap(Field field) {
        return new GraphQLFieldWrapper(field.name, collectSelectionSet("", field.selectionSet.children), collectArgumentMap("", field.arguments))
    }

    public static List<GraphQLFieldWrapper> wrap(Document document) {
        return document.children[0].children[0].children.stream().map({ child ->
            GraphQLFieldWrapper.wrap(child)
        }).collect()
    }
}
