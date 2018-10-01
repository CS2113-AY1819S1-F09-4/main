package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.CandidateBook;
import seedu.address.model.ReadOnlyCandidateBook;
import seedu.address.model.candidate.Candidate;

/**
 * An Immutable CandidateBook that is serializable to XML format
 */
@XmlRootElement(name = "addressbook")
public class XmlSerializableCandidateBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate candidate(s).";

    @XmlElement
    private List<XmlAdaptedCandidate> candidates;

    /**
     * Creates an empty XmlSerializableCandidateBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableCandidateBook() {
        candidates = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableCandidateBook(ReadOnlyCandidateBook src) {
        this();
        candidates.addAll(src.getCandidatelist().stream().map(XmlAdaptedCandidate::new).collect(Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code CandidateBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedCandidate}.
     */
    public CandidateBook toModelType() throws IllegalValueException {
        CandidateBook candidateBook = new CandidateBook();
        for (XmlAdaptedCandidate p : candidates) {
            Candidate candidate = p.toModelType();
            if (candidateBook.hasPerson(candidate)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            candidateBook.addPerson(candidate);
        }
        return candidateBook;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableCandidateBook)) {
            return false;
        }
        return candidates.equals(((XmlSerializableCandidateBook) other).candidates);
    }
}