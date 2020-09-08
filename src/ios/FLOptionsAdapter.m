#import "FLOptionsAdapter.h"
#if __has_include(<Fidel/Fidel-Swift.h>)
#import <Fidel/Fidel-Swift.h>
#elif __has_include("Fidel-Swift.h")
#import "Fidel-Swift.h"
#else
#import "Fidel/Fidel-Swift.h" // Required when used as a Pod in a Swift project
#endif
#import "FLRNCountryAdapter.h"
#import "FLImageAdapter.h"
#import "FLCardSchemesAdapter.h"


@interface FLOptionsAdapter()

@property (nonatomic, strong) id<FLRNCountryAdapter> countryAdapter;
@property (nonatomic, strong) id<FLImageAdapter> imageAdapter;
@property (nonatomic, strong) id<FLCardSchemesAdapter> cardSchemesAdapter;

@end

@implementation FLOptionsAdapter

- (instancetype)initWithCountryAdapter:(id<FLRNCountryAdapter>)countryAdapter
                          imageAdapter:(id<FLImageAdapter>)imageAdapter
                    cardSchemesAdapter:(id<FLCardSchemesAdapter>)cardSchemesAdapter {
    self = [super init];
    if (self) {
        _countryAdapter = countryAdapter;
        _imageAdapter = imageAdapter;
        _cardSchemesAdapter = cardSchemesAdapter;
    }
    return self;
}

NSString *const kCountryKey = @"Country";
NSString *const kOptionKey = @"Option";
NSString *const kBannerImageOptionKey = @"showBannerImage";
NSString *const kCountryOptionKey = @"country";
NSString *const kAutoScanOptionKey = @"autoScan";
NSString *const kCompanyNameOptionKey = @"companyName";
NSString *const kMetaDataOptionKey = @"metaData";
NSString *const kCardSchemesDataOptionKey = @"supportedCardSchemes";
NSString *const kProgramNameOptionKey = @"programName";
NSString *const kDeleteInstructionsOptionKey = @"deleteInstructions";
NSString *const kPrivacyURLOptionKey = @"privacyUrl";
NSString *const kTermsConditionsURLOptionKey = @"termsConditionsUrl";


- (void)setOptions:(NSDictionary *)options {
    NSArray *allOptionKeys = options.allKeys;
    if ([allOptionKeys containsObject:kBannerImageOptionKey]) {
        Boolean showBanner = [options[kBannerImageOptionKey] boolValue];
        if (showBanner) {
            UIImage *bannerImage = [self.imageAdapter processBannerImage];
            [FLFidel setBannerImage:bannerImage];
        }
    }
    
    if ([allOptionKeys containsObject:kCountryOptionKey]) {
        id rawCountry = options[kCountryOptionKey];
        FLFidel.country = [self.countryAdapter adaptedCountry:rawCountry];
    }
    
    if ([allOptionKeys containsObject:kAutoScanOptionKey]) {
        FLFidel.autoScan = [options[kAutoScanOptionKey] boolValue];
    }
    
    if ([allOptionKeys containsObject:kMetaDataOptionKey]) {
        id rawMetaData = options[kMetaDataOptionKey];
        if ([rawMetaData isKindOfClass:[NSDictionary class]]) {
            FLFidel.metaData = rawMetaData;
        }
    }
    if([allOptionKeys containsObject:kCardSchemesDataOptionKey]) {
        id rawData = options[kCardSchemesDataOptionKey];
        NSSet<NSNumber *> *supportedCardSchemes = [self.cardSchemesAdapter cardSchemesWithRawObject:rawData];
        FLFidel.objc_supportedCardSchemes = supportedCardSchemes;
    }
    
    FLFidel.companyName = [self getStringValueFor:kCompanyNameOptionKey fromDictionary:options];
    FLFidel.programName = [self getStringValueFor:kProgramNameOptionKey fromDictionary:options];
    FLFidel.deleteInstructions = [self getStringValueFor:kDeleteInstructionsOptionKey fromDictionary:options];
    FLFidel.privacyURL = [self getStringValueFor:kPrivacyURLOptionKey fromDictionary:options];
    FLFidel.termsConditionsURL = [self getStringValueFor:kTermsConditionsURLOptionKey fromDictionary:options];
}

- (NSString * _Nullable)getStringValueFor:(NSString *)key fromDictionary:(NSDictionary *)dict {
    NSArray *allKeys = dict.allKeys;
    if ([allKeys containsObject:key]) {
        id value = dict[key];
        if ([value isKindOfClass:[NSString class]]) {
            return value;
        }
        else {
            return [value stringValue];
        }
    }
    return nil;
}

@end