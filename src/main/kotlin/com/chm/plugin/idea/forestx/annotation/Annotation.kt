package com.chm.plugin.idea.forestx.annotation

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Iterables
import com.google.common.collect.Maps
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import java.util.*

/**
 * @author caihongming
 * @version v1.0
 * @since 2022-08-25
 **/
class Annotation(
    private val label: String,
    val qualifiedName: String
) : Cloneable {

    companion object {

        /**
         * The constant BACKEND.
         */
        val BACKEND = Annotation("@Backend", "com.dtflys.forest.annotation.Backend")

        /**
         * The constant HEADERS.
         */
        val HEADERS = Annotation("@Headers", "com.dtflys.forest.annotation.Headers")

        /**
         * The constant ADDRESS.
         */
        val ADDRESS = Annotation("@Address", "com.dtflys.forest.annotation.Address")

        /**
         * The constant REQUEST.
         */
        val REQUEST = Annotation("@Request", "com.dtflys.forest.annotation.Request")

        /**
         * The constant GET.
         */
        val GET = Annotation("@Get", "com.dtflys.forest.annotation.Get")

        /**
         * The constant GET_REQUEST.
         */
        val GET_REQUEST = Annotation("@GetRequest", "com.dtflys.forest.annotation.GetRequest")

        /**
         * The constant POST.
         */
        val POST = Annotation("@Post", "com.dtflys.forest.annotation.Post")

        /**
         * The constant POST_REQUEST.
         */
        val POST_REQUEST = Annotation("@PostRequest", "com.dtflys.forest.annotation.PostRequest")

        /**
         * The constant PUT.
         */
        val PUT = Annotation("@Put", "com.dtflys.forest.annotation.Put")

        /**
         * The constant PUT_REQUEST.
         */
        val PUT_REQUEST = Annotation("@PutRequest", "com.dtflys.forest.annotation.PutRequest")

        /**
         * The constant PATCH.
         */
        val PATCH = Annotation("@Patch", "com.dtflys.forest.annotation.Patch")

        /**
         * The constant PATCH_REQUEST.
         */
        val PATCH_REQUEST = Annotation("@PatchRequest", "com.dtflys.forest.annotation.PatchRequest")

        /**
         * The constant HEAD_REQUEST.
         */
        val HEAD_REQUEST = Annotation("@HeadRequest", "com.dtflys.forest.annotation.HeadRequest")

        /**
         * The constant OPTIONS.
         */
        val OPTIONS = Annotation("@Options", "com.dtflys.forest.annotation.Options")

        /**
         * The constant OPTIONS_REQUEST.
         */
        val OPTIONS_REQUEST = Annotation("@OptionsRequest", "com.dtflys.forest.annotation.OptionsRequest")

        /**
         * The constant DELETE.
         */
        val DELETE = Annotation("@Delete", "com.dtflys.forest.annotation.Delete")

        /**
         * The constant DELETE_REQUEST.
         */
        val DELETE_REQUEST = Annotation("@DeleteRequest", "com.dtflys.forest.annotation.DeleteRequest")

        /**
         * The constant TRACE.
         */
        val TRACE = Annotation("@Trace", "com.dtflys.forest.annotation.Trace")

        /**
         * The constant TRACE_REQUEST.
         */
        val TRACE_REQUEST = Annotation("@TraceRequest", "com.dtflys.forest.annotation.TraceRequest")

        /**
         * The constant AUTOWIRED.
         */
        val AUTOWIRED = Annotation("@Autowired", "org.springframework.beans.factory.annotation.Autowired")

        /**
         * The constant RESOURCE.
         */
        val RESOURCE = Annotation("@Resource", "javax.annotation.Resource")

        /**
         * The constant STATEMENT_SYMMETRIES.
         */
        val FOREST_METHOD_ANNOTATION: Set<Annotation> = ImmutableSet.of(
            REQUEST, GET,
            GET_REQUEST, POST, POST_REQUEST, PUT, PUT_REQUEST, HEAD_REQUEST, OPTIONS, OPTIONS_REQUEST, PATCH,
            PATCH_REQUEST, DELETE, DELETE_REQUEST, TRACE, TRACE_REQUEST
        )
    }

    private var attributePairs: MutableMap<String, AnnotationValue> = Maps.newHashMap()

    private fun addAttribute(key: String, value: AnnotationValue): Annotation {
        attributePairs[key] = value
        return this
    }

    /**
     * With attribute annotation.
     *
     * @param key   the key
     * @param value the value
     * @return the annotation
     */
    fun withAttribute(key: String, value: AnnotationValue): Annotation {
        val copy = clone()
        copy.attributePairs = Maps.newHashMap(attributePairs)
        return copy.addAttribute(key, value)
    }

    /**
     * With value annotation.
     *
     * @param value the value
     * @return the annotation
     */
    fun withValue(value: AnnotationValue): Annotation? {
        return withAttribute("value", value)
    }

    override fun toString(): String {
        val builder = StringBuilder(label)
        if (!Iterables.isEmpty(attributePairs.entries)) {
            builder.append(setupAttributeText())
        }
        return builder.toString()
    }

    private fun setupAttributeText(): String? {
        val singleValue = getSingleValue()
        return singleValue.orElseGet { getComplexValue() }
    }

    private fun getComplexValue(): String {
        val builder = StringBuilder("(")
        for (key in attributePairs.keys) {
            builder.append(key)
            builder.append(" = ")
            builder.append(attributePairs[key].toString())
            builder.append(", ")
        }
        builder.deleteCharAt(builder.length - 1)
        builder.deleteCharAt(builder.length - 1)
        builder.append(")")
        return builder.toString()
    }

    /**
     * To psi class optional.
     *
     * @param project the project
     * @return the optional
     */
    fun toPsiClass(project: Project): Optional<PsiClass> {
        return Optional.ofNullable(
            JavaPsiFacade.getInstance(project).findClass(qualifiedName, GlobalSearchScope.allScope(project))
        )
    }

    private fun getSingleValue(): Optional<String> {
        return try {
            val value = Iterables.getOnlyElement(attributePairs.keys)
            val builder = StringBuilder("(")
            builder.append(attributePairs[value].toString())
            builder.append(")")
            Optional.of(builder.toString())
        } catch (e: Exception) {
            Optional.empty()
        }
    }

    override fun clone(): Annotation {
        return try {
            super.clone() as Annotation
        } catch (e: CloneNotSupportedException) {
            throw IllegalStateException()
        }
    }
}


/**
 * The interface Annotation value.
 */
interface AnnotationValue

/**
 * The type String value.
 */
class StringValue(private val value: String) : AnnotationValue {
    override fun toString(): String {
        return "\"" + value + "\""
    }
}