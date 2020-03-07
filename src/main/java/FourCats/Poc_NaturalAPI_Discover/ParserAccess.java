package FourCats.Poc_NaturalAPI_Discover;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

public class ParserAccess implements ParserAccessInterface{
	protected StanfordCoreNLP pipeline;

	//private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	private final static String DP_MODEL = "edu/stanford/nlp/models/parser/nndep/english_UD.gz";
	
	private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
	
	//private final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
	
	private final DependencyParser depparser = DependencyParser.loadFromModelFile(DP_MODEL);
	
	public ParserAccess() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 

        /*
         * This is a pipeline that takes in a string and returns various analyzed linguistic forms. 
         * The String is tokenized via a tokenizer (such as PTBTokenizerAnnotator), 
         * and then other sequence model style annotation can be used to add things like lemmas, 
         * POS tags, and named entities. These are returned as a list of CoreLabels. 
         * Other analysis components build and store parse trees, dependency graphs, etc. 
         * 
         * This class is designed to apply multiple Annotators to an Annotation. 
         * The idea is that you first build up the pipeline by adding Annotators, 
         * and then you take the objects you wish to annotate and pass them in and 
         * get in return a fully annotated object.
         * 
         *  StanfordCoreNLP loads a lot of models, so you probably
         *  only want to do this once per execution
         */
        this.pipeline = new StanfordCoreNLP(props);
    }
	public ParserData parseSentence(String documentContent) {

        Annotation document = new Annotation(documentContent);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        ParserData data = new ParserData();

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            GrammaticalStructure gramstruct = depparser.predict(sentence);
            Collection<TypedDependency> dependencies = gramstruct.typedDependencies();
            for(TypedDependency dep : dependencies) {
                //parola principale
                //System.out.println(dep);

                if(dep.reln().getShortName().equalsIgnoreCase("dobj")) {
                    data.addElement(dep.gov().lemma()+ " " + dep.dep().lemma());
                }
                //parola dipendente
                //System.out.println(dep.dep());
                //relazione
                //System.out.println(dep.reln());
            }
            // Iterate over all tokens in a sentence
            /*for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
            }*/
        }
        //System.out.println("----------- /TypedDependency/ -----------");
        /*CoreMap tokens = tokenize(sentence);
        Tree tree = depparser.predict(sentence);
        List<Tree> leaves = tree.getLeaves();
        for (Tree leaf : leaves) {
            Tree parent = leaf.parent(tree);
            System.out.print(leaf.label().value() + "-" + parent.label().value() + " ");
        }*/
        return data;
	}
	
	private List<CoreLabel> tokenize(String str){
		Tokenizer<CoreLabel> tokenizer =
	            tokenizerFactory.getTokenizer(
	                new StringReader(str));    
	        return tokenizer.tokenize();
	}
}
