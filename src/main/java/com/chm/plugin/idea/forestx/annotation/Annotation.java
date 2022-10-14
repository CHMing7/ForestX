package com.chm.plugin.idea.forestx.annotation;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.jgoodies.common.base.Strings;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-03-11
 **/
public class Annotation implements Cloneable {

    /**
     * The constant BACKEND.
     */
    public static final Annotation BACKEND = new Annotation("@Backend", "com.dtflys.forest.annotation.Backend");

    /**
     * The constant HEADERS.
     */
    public static final Annotation HEADERS = new Annotation("@Headers", "com.dtflys.forest.annotation.Headers");

    /**
     * The constant ADDRESS.
     */
    public static final Annotation ADDRESS = new Annotation("@Address", "com.dtflys.forest.annotation.Address");

    /**
     * The constant REQUEST.
     */
    public static final Annotation REQUEST = new Annotation("@Request", "com.dtflys.forest.annotation.Request");

    /**
     * The constant GET.
     */
    public static final Annotation GET = new Annotation("@Get", "com.dtflys.forest.annotation.Get");

    /**
     * The constant GET_REQUEST.
     */
    public static final Annotation GET_REQUEST = new Annotation("@GetRequest", "com.dtflys.forest.annotation.GetRequest");

    /**
     * The constant POST.
     */
    public static final Annotation POST = new Annotation("@Post", "com.dtflys.forest.annotation.Post");

    /**
     * The constant POST_REQUEST.
     */
    public static final Annotation POST_REQUEST = new Annotation("@PostRequest", "com.dtflys.forest.annotation.PostRequest");

    /**
     * The constant PUT.
     */
    public static final Annotation PUT = new Annotation("@Put", "com.dtflys.forest.annotation.Put");

    /**
     * The constant PUT_REQUEST.
     */
    public static final Annotation PUT_REQUEST = new Annotation("@PutRequest", "com.dtflys.forest.annotation.PutRequest");

    /**
     * The constant PATCH.
     */
    public static final Annotation PATCH = new Annotation("@Patch", "com.dtflys.forest.annotation.Patch");

    /**
     * The constant PATCH_REQUEST.
     */
    public static final Annotation PATCH_REQUEST = new Annotation("@PatchRequest", "com.dtflys.forest.annotation.PatchRequest");

    /**
     * The constant HEAD_REQUEST.
     */
    public static final Annotation HEAD_REQUEST = new Annotation("@HeadRequest", "com.dtflys.forest.annotation.HeadRequest");

    /**
     * The constant OPTIONS.
     */
    public static final Annotation OPTIONS = new Annotation("@Options", "com.dtflys.forest.annotation.Options");

    /**
     * The constant OPTIONS_REQUEST.
     */
    public static final Annotation OPTIONS_REQUEST = new Annotation("@OptionsRequest", "com.dtflys.forest.annotation.OptionsRequest");

    /**
     * The constant DELETE.
     */
    public static final Annotation DELETE = new Annotation("@Delete", "com.dtflys.forest.annotation.Delete");

    /**
     * The constant DELETE_REQUEST.
     */
    public static final Annotation DELETE_REQUEST = new Annotation("@DeleteRequest", "com.dtflys.forest.annotation.DeleteRequest");

    /**
     * The constant TRACE.
     */
    public static final Annotation TRACE = new Annotation("@Trace", "com.dtflys.forest.annotation.Trace");

    /**
     * The constant TRACE_REQUEST.
     */
    public static final Annotation TRACE_REQUEST = new Annotation("@TraceRequest", "com.dtflys.forest.annotation.TraceRequest");


    /**
     * The constant TRACE_REQUEST
     */
    public static final Annotation BASE_REQUEST = new Annotation("@BaseRequest", "com.dtflys.forest.annotation.BaseRequest");

    // 参数注解

    /**
     * The constant VAR
     */
    public static final Annotation VAR = new Annotation("@Var", "com.dtflys.forest.annotation.Var");

    /**
     * The constant AUTOWIRED.
     */
    public static final Annotation AUTOWIRED = new Annotation("@Autowired", "org.springframework.beans.factory.annotation.Autowired");

    /**
     * The constant RESOURCE.
     */
    public static final Annotation RESOURCE = new Annotation("@Resource", "javax.annotation.Resource");

    /**
     * The constant STATEMENT_SYMMETRIES.
     */
    public static final Set<Annotation> FOREST_METHOD_ANNOTATION = ImmutableSet.of(REQUEST, GET,
            GET_REQUEST, POST, POST_REQUEST, PUT, PUT_REQUEST, HEAD_REQUEST, OPTIONS, OPTIONS_REQUEST, PATCH,
            PATCH_REQUEST, DELETE, DELETE_REQUEST, TRACE, TRACE_REQUEST, ADDRESS, HEADERS);

    public static final Set<Annotation> FOREST_INTERFACE_ANNOTATION = ImmutableSet.of(BASE_REQUEST);


    public static final Set<String> FOREST_ANNOTATION_CLASSES = new HashSet<>();

    static {
        for (Annotation annotation : FOREST_INTERFACE_ANNOTATION) {
            FOREST_ANNOTATION_CLASSES.add(annotation.getQualifiedName());
        }
        for (Annotation annotation : FOREST_METHOD_ANNOTATION) {
            FOREST_ANNOTATION_CLASSES.add(annotation.getQualifiedName());
        }
    }

    private final String label;

    private final String qualifiedName;

    private Map<String, AnnotationValue> attributePairs;


    public static boolean isForestAnnotation(PsiAnnotation annotation) {
        if (annotation == null) {
            return false;
        }
        final String qualifiedName = annotation.getQualifiedName();
        if (qualifiedName.startsWith("com.dtflys.forest")) {
            return true;
        }
        PsiClass annotationType = annotation.resolveAnnotationType();
        PsiAnnotation[] typeAnnotations = annotationType.getAnnotations();
        for (PsiAnnotation typeAnnotation : typeAnnotations) {
            final String className = typeAnnotation.getQualifiedName();
            if (className.equals("com.dtflys.forest.annotation.MethodLifeCycle") ||
                    className.equals("com.dtflys.forest.annotation.ParamLifeCycle") ||
                    className.equals("com.dtflys.forest.annotation.BaseLifeCycle")) {
                return true;
            }
        }
        return false;
    }


    /**
     * Instantiates a new Annotation.
     *
     * @param label         the label
     * @param qualifiedName the qualified name
     */
    public Annotation(@NotNull String label, @NotNull String qualifiedName) {
        this.label = label;
        this.qualifiedName = qualifiedName;
        attributePairs = Maps.newHashMap();
    }

    private Annotation addAttribute(String key, AnnotationValue value) {
        this.attributePairs.put(key, value);
        return this;
    }

    /**
     * With attribute annotation.
     *
     * @param key   the key
     * @param value the value
     * @return the annotation
     */
    public Annotation withAttribute(@NotNull String key, @NotNull AnnotationValue value) {
        Annotation copy = this.clone();
        copy.attributePairs = Maps.newHashMap(this.attributePairs);
        return copy.addAttribute(key, value);
    }

    /**
     * With value annotation.
     *
     * @param value the value
     * @return the annotation
     */
    public Annotation withValue(@NotNull AnnotationValue value) {
        return withAttribute("value", value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(label);
        if (!Iterables.isEmpty(attributePairs.entrySet())) {
            builder.append(setupAttributeText());
        }
        return builder.toString();
    }

    private String setupAttributeText() {
        Optional<String> singleValue = getSingleValue();
        return singleValue.orElseGet(this::getComplexValue);
    }

    private String getComplexValue() {
        StringBuilder builder = new StringBuilder("(");
        for (String key : attributePairs.keySet()) {
            builder.append(key);
            builder.append(" = ");
            builder.append(attributePairs.get(key).toString());
            builder.append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        return builder.toString();
    }

    /**
     * To psi class optional.
     *
     * @param project the project
     * @return the optional
     */
    public Optional<PsiClass> toPsiClass(@NotNull Project project) {
        return Optional.ofNullable(JavaPsiFacade.getInstance(project).findClass(getQualifiedName(), GlobalSearchScope.allScope(project)));
    }

    private Optional<String> getSingleValue() {
        try {
            String value = Iterables.getOnlyElement(attributePairs.keySet());
            StringBuilder builder = new StringBuilder("(");
            builder.append(attributePairs.get(value).toString());
            builder.append(")");
            return Optional.of(builder.toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Gets label.
     *
     * @return the label
     */
    @NotNull
    public String getLabel() {
        return label;
    }

    /**
     * Gets qualified name.
     *
     * @return the qualified name
     */
    @NotNull
    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    protected Annotation clone() {
        try {
            return (Annotation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

    /**
     * The interface Annotation value.
     */
    public interface AnnotationValue {
    }

    /**
     * The type String value.
     */
    public static class StringValue implements AnnotationValue {

        private final String value;

        /**
         * Instantiates a new String value.
         *
         * @param value the value
         */
        public StringValue(@NotNull String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "\"" + value + "\"";
        }

    }

}
